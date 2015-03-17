
package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.steer.behaviors.ReachOrientation;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.ResourceComponent;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.components.WeaponComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.ecs.EntityCategory;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.Constants.BUILD;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.Location2;

public enum UnitState implements State<Entity> {

	FORMATION() {

		@Override
		public void enter (Entity entity) {
			super.enter(entity);

			SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);
			SteerableComponent steerable = Components.STEERABLE.get(entity);
			Location2 targetLocation = (Location2)Components.UNIT.get(entity).getTargetLocation();

			Arrive<Vector2> arriveSB = new Arrive<Vector2>(steerable, targetLocation).setTimeToTarget(0.001f)
				.setArrivalTolerance(0.01f).setDecelerationRadius(2f);

			ReachOrientation<Vector2> reachOrientationSB = new ReachOrientation<Vector2>(steerable, targetLocation)
				.setTimeToTarget(0.001f).setAlignTolerance(0.001f).setDecelerationRadius(MathUtils.PI);

			BlendedSteering<Vector2> blendSB = new BlendedSteering<Vector2>(steerable);
			blendSB.add(arriveSB, 1.0f);
			blendSB.add(reachOrientationSB, 1.0f);
			behavior.setBehavior(blendSB);
		}

	},

	HARVEST_IDLE() {
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			// Request a target from our squad
			Entity squad = Components.UNIT.get(entity).getSquad();
			// This signals the squad to give us a new target
			MessageManager.getInstance().dispatchMessage(null, Components.FSM.get(squad),
				TelegramMessage.UNIT_TARGET_REQUEST.ordinal(), entity);

			// If we were delegated a target from our squad lets harvest. Otherwise follow the formation.
			if (Components.TARGET.get(entity).getTarget() != null) Components.FSM.get(entity).changeState(HARVEST);
		}
	},

	HARVEST() {
		@Override
		public void enter (Entity entity) {
			super.enter(entity);

			// Get relevant components
			Entity target = Components.TARGET.get(entity).getTarget();
			SteerableComponent steerable = Components.STEERABLE.get(entity);
			SteerableComponent targetSteerable = Components.STEERABLE.get(target);
			Entity squad = Components.UNIT.get(entity).getSquad();
			SquadComponent squadComp = Components.SQUAD.get(squad);

			// Steering behavior
			Arrive<Vector2> arrive = new Arrive<Vector2>(steerable).setTarget(targetSteerable).setTimeToTarget(0.01f)
				.setArrivalTolerance(0.001f).setDecelerationRadius(4f);

			Face<Vector2> faceSB = new Face<Vector2>(steerable).setTarget(targetSteerable).setAlignTolerance(0.001f)
				.setTimeToTarget(0.001f).setDecelerationRadius(2.0f);

			Array<Steerable<Vector2>> collisionAgents = new Array<Steerable<Vector2>>();
			collisionAgents.addAll(squadComp.resourceAgents);
			collisionAgents.addAll(Components.SQUAD.get(squad).memberAgents);

			RadiusProximity<Vector2> radiusProximity = new RadiusProximity<Vector2>(steerable, collisionAgents, 0.1f);
			CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(steerable, radiusProximity);

			BlendedSteering<Vector2> blendSB = new BlendedSteering<Vector2>(steerable);
			blendSB.add(arrive, 1.0f);
			blendSB.add(faceSB, 1.0f);
			blendSB.add(collisionAvoidanceSB, 1000.0f);

			SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);
			behavior.setBehavior(blendSB);
		}

		public void update (Entity entity) {
			super.update(entity);

			// Get relevant components
			Entity targetResource = Components.TARGET.get(entity).getTarget();
			SteerableComponent steerable = Components.STEERABLE.get(entity);
			SteerableComponent targetSteerable = Components.STEERABLE.get(targetResource);

			// If were in position harvest the resource
			if (targetSteerable.getPosition().dst(steerable.getPosition()) <= targetSteerable.getBoundingRadius()
				+ Constants.unitRadius * 2.0f) {
				ResourceComponent targetResourceComp = Components.RESOURCE.get(targetResource);
				targetResourceComp.value -= Constants.resourceCollectionSpeed;

				//Check if we have effectively destroyed our target
			}
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			TelegramMessage telegramMsg = TelegramMessage.values()[telegram.message];
			switch (telegramMsg) {

			case TARGET_REMOVED:
				Components.FSM.get(entity).changeState(HARVEST_IDLE);
				return true;

			default:
				return false;
			}
		}

	},

	COMBAT_IDLE() {
		@Override
		public void enter (Entity entity) {
			super.enter(entity);

			Entity squad = Components.UNIT.get(entity).getSquad();
			// Sends a request to the squad for a new target
			MessageManager.getInstance().dispatchMessage(null, Components.FSM.get(squad),
				TelegramMessage.UNIT_TARGET_REQUEST.ordinal(), entity);

			// If we were delegated a target lets fight. Otherwise follow the formation
			if (Components.TARGET.get(entity).getTarget() != null)
				Components.FSM.get(entity).changeState(COMBAT);
			else
				Components.FSM.get(entity).changeState(FORMATION);
		}

	},

	COMBAT() {

		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			// When we enter this state we already have a target

			// Get our relevant components
			SteerableComponent steerable = Components.STEERABLE.get(entity);
			TargetComponent targetComp = Components.TARGET.get(entity);
			SteeringBehaviorComponent behaviorComp = Components.STEERING_BEHAVIOR.get(entity);
			Location2 targetLocation = (Location2)Components.UNIT.get(entity).getTargetLocation();

			// Steering Behaviors
			Face<Vector2> faceSB = new Face<Vector2>(steerable).setTarget(Components.STEERABLE.get(targetComp.getTarget()))
				.setAlignTolerance(0.0001f).setDecelerationRadius(2f).setTimeToTarget(0.00001f);

			Arrive<Vector2> arriveSB = new Arrive<Vector2>(steerable, targetLocation).setTimeToTarget(0.001f)
				.setArrivalTolerance(0.01f).setDecelerationRadius(2f);

			BlendedSteering<Vector2> blendSB = new BlendedSteering<Vector2>(steerable);
			blendSB.add(arriveSB, 1.0f);
			blendSB.add(faceSB, 1.0f);
			behaviorComp.setBehavior(blendSB);
		}

		@Override
		public void update (Entity entity) {
			super.update(entity);
			// Grab our target and our weapon
			Entity squad = Components.UNIT.get(entity).getSquad();
			Entity target = Components.TARGET.get(entity).getTarget();
			WeaponComponent weaponComp = Components.WEAPON.get(entity);

			if (weaponComp.cooldown <= 0) {
				// Our weapon is off cooldown and we are ready to fire
				SteerableComponent steerable = Components.STEERABLE.get(entity);
				SteerableComponent targetSteerable = Components.STEERABLE.get(target);

				// Get the angle between our target and us and our current orientation
				Vector2 position = steerable.getPosition();
				Vector2 targetPosition = targetSteerable.getPosition();
				float angle = targetPosition.sub(position).angleRad();
				float orientation = steerable.getOrientation();

				if ((entity.flags & EntityCategory.MOTHERSHIP) == EntityCategory.MOTHERSHIP) {
					// Since we are a mothership we don't care if were facing the right direction
					orientation = angle; // We will fire our weapon directly at our target
				}

				// If were facing the angle between us and our target
				if (true /** MathUtils.isEqual(orientation, angle, (MathUtils.PI / 4)) */
				) {

					// Set the position of the projectile we will fire
					float radius = steerable.getBoundingRadius();
					position.add((radius * MathUtils.cos(orientation)), radius * MathUtils.sin(orientation));

					// Set the velocity of the projectile we will fire
					Vector2 velocity = Vector2.X;
					velocity.setAngleRad(orientation);
					velocity.nor();
					velocity.scl(weaponComp.projectileVelocity);

					// Spawn our projetile into the gameWorld
					Faction faction = Components.FACTION.get(entity).getFaction();
					EntityFactory.createProjectile(position, velocity, weaponComp.projectileRadius, faction, (int)weaponComp.damage);

					weaponComp.cooldown = weaponComp.attackSpeed; // Set the cooldown to the attack speed
				}
			}

			weaponComp.cooldown -= Gdx.graphics.getDeltaTime();
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			TelegramMessage telegramMsg = TelegramMessage.values()[telegram.message];
			switch (telegramMsg) {
			case TARGET_REMOVED:
				Components.FSM.get(entity).changeState(COMBAT_IDLE);
				return true;
			default:
				return false;
			}
		}
	};

	private static final String TAG = UnitState.class.getSimpleName();

	@Override
	public void enter (Entity entity) {
		if (Constants.build == BUILD.DEV) {
			State<Entity> state = Components.FSM.get(entity).getStateMachine().getCurrentState();
			Gdx.app.debug(TAG, "Entered: " + state.toString());
		}
	}

	@Override
	public void update (Entity entity) {

	}

	@Override
	public void exit (Entity entity) {

	}

	@Override
	public boolean onMessage (Entity entity, Telegram telegram) {
		return false;
	}
}
