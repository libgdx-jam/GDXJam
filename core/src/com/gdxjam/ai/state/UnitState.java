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
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.Location2;

public enum UnitState implements State<Entity>{
	
	IDLE(){
		@Override
		public void enter(Entity entity) {
			super.enter(entity);
			//Get our FSM
			FSMComponent fsmComp = Components.FSM.get(entity);
			
			//Dispatch the message to our squad that we need the new target

			//After the message had been dispatched we see if the squad has delegated us a target
			if(fsmComp.getStateMachine().getPreviousState() != IDLE){
				//Now we just follow the formation
				SteerableComponent steerable = Components.STEERABLE.get(entity);
				Location2 targetLocation = (Location2)Components.UNIT.get(entity).getTargetLocation();
				
				Arrive<Vector2> arriveSB = new Arrive<Vector2>(steerable, targetLocation)
						.setTimeToTarget(0.001f)
						.setArrivalTolerance(0.01f)
						.setDecelerationRadius(2f);
				
				ReachOrientation<Vector2> reachOrientationSB = new ReachOrientation<Vector2>(steerable, targetLocation)
						.setTimeToTarget(0.001f)
						.setAlignTolerance(0.001f)
						.setDecelerationRadius(MathUtils.PI);
				
				BlendedSteering<Vector2> blendSB = new BlendedSteering<Vector2>(steerable);
				blendSB.add(arriveSB, 1.0f);
				blendSB.add(reachOrientationSB, 1.0f);
				Components.STEERING_BEHAVIOR.get(entity).setBehavior(blendSB);
			}
		}
	},
	
	FIND_TARGET(){
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			FSMComponent fsmComp = Components.FSM.get(entity);
			Entity squad = Components.UNIT.get(entity).getSquad();
			FSMComponent squadFSM = Components.FSM.get(squad);
			MessageManager.getInstance().dispatchMessage(null, squadFSM, TelegramMessage.TARGET_REQUEST.ordinal(), entity);
			
			//When we enter the idle state we request a target from our squad to see if there is anything that needs doing.
			Entity target = Components.TARGET.get(entity).getTarget();
			if(target != null){
				//The target request was successful and we now have a target
				if((target.flags & EntityCategory.RESOURCE) == EntityCategory.RESOURCE)
					fsmComp.changeState(HARVEST);//The target is a resource so we now enter the HARVEST state
				else if ((target.flags & EntityCategory.UNIT) == EntityCategory.UNIT)
					fsmComp.changeState(COMBAT);
			} else {
				fsmComp.changeState(IDLE);
			}
		}
	},
	
	HARVEST(){
		@Override
		public void enter(Entity entity) {
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
		
		@Override
		public void update(Entity entity) {
			super.update(entity);
			
			// Get relevant components
			Entity targetResource = Components.TARGET.get(entity).getTarget();
			if(targetResource == null) return;
			SteerableComponent steerable = Components.STEERABLE.get(entity);
			SteerableComponent targetSteerable = Components.STEERABLE.get(targetResource);
			if(targetSteerable == null) return;

			// If were in position harvest the resource
			if (targetSteerable.getPosition().dst(steerable.getPosition()) <= targetSteerable.getBoundingRadius()
				+ Constants.unitRadius * 2.0f) {
				ResourceComponent targetResourceComp = Components.RESOURCE.get(targetResource);
				targetResourceComp.value -= Constants.resourceCollectionSpeed;

			}
		}
	},
	
	COMBAT(){
		@Override
		public void enter(Entity entity) {
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
			Entity target = Components.TARGET.get(entity).getTarget();
			if(target == null){ 
				Gdx.app.debug(TAG, "our target is null in combat!");
				return;
			}	//Double check to make sure we have a target
			
			WeaponComponent weaponComp = Components.WEAPON.get(entity);

			if (weaponComp.cooldown <= 0) {
				// Our weapon is off cooldown and we are ready to fire
				SteerableComponent steerable = Components.STEERABLE.get(entity);
				SteerableComponent targetSteerable = Components.STEERABLE.get(target);

				// Get the angle between our target and us and our current orientation
				Vector2 position = steerable.getPosition();
				Vector2 displacement = targetSteerable.getPosition().cpy().sub(position);
				float angle = displacement.angleRad();
				float orientation = steerable.getOrientation();

				if ((entity.flags & EntityCategory.MOTHERSHIP) == EntityCategory.MOTHERSHIP) {
					// Since we are a mothership we don't care if were facing the right direction
					orientation = angle; // We will fire our weapon directly at our target
				}
				
				// The player unit will have aimbot
				if(Components.FACTION.get(entity).getFaction() == Constants.playerFaction){
					float timeStep = displacement.len() / weaponComp.projectileVelocity;
					Vector2 deltaPosition = steerable.getLinearVelocity().cpy().scl(timeStep);
					Vector2 perdictedPosition = targetSteerable.getPosition().cpy().add(deltaPosition);
					orientation = perdictedPosition.sub(position).angleRad();
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
	}
	
	;

	private static final String TAG = "[UnitState]";
	
	@Override
	public void enter(Entity entity) {
//		Gdx.app.log("UnitState: ", Components.FSM.get(entity).getStateMachine().getCurrentState().toString());
		
	}

	@Override
	public void update(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		TelegramMessage telegramMsg = TelegramMessage.values()[telegram.message];
		switch (telegramMsg) {
		
		/* If our target was removed from the engine
		 * we go back to idle and request a new target from our squad
		 */
		case TARGET_REMOVED:{
//			Gdx.app.error(TAG, "TARGET REMOVED!");
			Components.FSM.get(entity).changeState(FIND_TARGET);
			return true;
		}
		
		//The message was not handled by this state
		default:
			return false;
		}
	}

}
