
package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.utils.Constants;

public enum SquadCombatState implements State<Entity> {

	IDLE() {
		@Override
		public void enter (Entity entity) {
			SquadComponent squadComp = Components.SQUAD.get(entity);
			
			if (squadComp.enemiesInRange.size > 0) {
				Components.TARGET.get(entity).setTarget(squadComp.enemiesInRange.random());

				// Set the squads state to a combat state depending on if were the player
				Faction faction = Components.FACTION.get(entity).getFaction();
				SquadCombatState state = faction == Constants.playerFaction ?  PLAYER : AI_AGRESSIVE;
				Components.FSM.get(entity).changeState(state);
			} else {
				// If we have targets available we don't need to be idle
				for (Entity member : squadComp.members) {
					Components.FSM.get(member).changeState(UnitState.IDLE);
				}
			}

		}

	},

	AI_AGRESSIVE() {
		@Override
		public void enter (Entity entity) {
			super.enter(entity); // Tell our units we are fighting

			SteerableComponent steerable = Components.STEERABLE.get(entity);
			Entity targetSquad = Components.TARGET.get(entity).getTarget();
			SteerableComponent targetSteerable = Components.STEERABLE.get(targetSquad);

			Wander<Vector2> wanderSB = new Wander<Vector2>(steerable).setAlignTolerance(0.1f).setWanderOffset(2.5f)
				.setWanderOrientation(0).setWanderRate(2.5f);

			Pursue<Vector2> pursueSB = new Pursue<Vector2>(steerable, targetSteerable).setMaxPredictionTime(0.1f);

			BlendedSteering<Vector2> blendSB = new BlendedSteering<Vector2>(steerable);
			blendSB.add(wanderSB, 0.8f);
			blendSB.add(pursueSB, 1.0f);

			Components.STEERING_BEHAVIOR.get(entity).setBehavior(blendSB);
		}

		@Override
		public void update (Entity entity) {
			float distance = distanceToTarget(entity);
			if (distance <= SquadTatics.DEFENSIVE_RADIUS) Components.FSM.get(entity).changeState(AI_DEFENSIVE);
		}

	},

	AI_DEFENSIVE() {
		@Override
		public void enter (Entity entity) {
			super.enter(entity); // Tell our units were fighting

			Entity targetSquad = Components.TARGET.get(entity).getTarget();
			SteerableComponent targetSteerable = Components.STEERABLE.get(targetSquad);
			SteerableComponent steerable = Components.STEERABLE.get(entity);

			Wander<Vector2> wanderSB = new Wander<Vector2>(steerable).setAlignTolerance(0.1f).setWanderOffset(2.5f)
				.setWanderOrientation(0).setWanderRate(2.5f);

			Evade<Vector2> evadeSB = new Evade<Vector2>(steerable, targetSteerable, 0.1f);

			BlendedSteering<Vector2> blendSB = new BlendedSteering<Vector2>(steerable);
			blendSB.add(wanderSB, 0.8f);
			blendSB.add(evadeSB, 1.0f);

			Components.STEERING_BEHAVIOR.get(entity).setBehavior(evadeSB);
		}

		@Override
		public void update (Entity entity) {
			float distance = distanceToTarget(entity);

			if (distance >= SquadTatics.AGRESSIVE_RADIUS) {
				Components.FSM.get(entity).changeState(AI_AGRESSIVE);
			}
		}
	},

	PLAYER() {
	// We do nothing... this is all up to the player
	}

	;

	/** Gets the distance between us and our target
	 * @param entity
	 * @return */
	public float distanceToTarget (Entity entity) {
		Entity targetSquad = Components.TARGET.get(entity).getTarget();
		SteerableComponent targetSteerable = Components.STEERABLE.get(targetSquad);
		SteerableComponent steerable = Components.STEERABLE.get(entity);
		return targetSteerable.getPosition().dst(steerable.getPosition());
	}

	/** Default enter behavior for a SquadCombatState */
	@Override
	public void enter (Entity entity) {
		SquadComponent squadComp = Components.SQUAD.get(entity);

		// Our units are now in a combat state and will query us for targets
		for (int i = 0; i < squadComp.members.size; i++) {
			Entity member = squadComp.members.get(i);
			Components.FSM.get(member).changeState(UnitState.IDLE);
		}

	}

	@Override
	public void update (Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit (Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMessage (Entity entity, Telegram telegram) {
		SquadComponent squadComp = Components.SQUAD.get(entity);

		TelegramMessage telegramMsg = TelegramMessage.values()[telegram.message];
		switch (telegramMsg) {

		/** If we have found a new target we need to assess its threat level to decide if we need to switch targets. */
		case SQUAD_DISCOVERED_ENEMY:
			Entity target = Components.TARGET.get(entity).getTarget();

			if (target == null) {
				target = squadComp.enemiesInRange.random();
				Components.TARGET.get(entity).setTarget(target);
				
				if (Components.FACTION.get(entity).getFaction() != Constants.playerFaction)
					Components.FSM.get(entity).changeState(SquadCombatState.AI_AGRESSIVE);
				else
					Components.FSM.get(entity).changeState(SquadCombatState.PLAYER);
			} else {
				// TODO assess threat level of new targets
				
				
			}

			return true;

		case TARGET_REQUEST: {
			//Check if the squad has targets to give
			Entity targetSquad = Components.TARGET.get(entity).getTarget();
			if(targetSquad == null){
				Components.FSM.get(entity).changeState(IDLE);
			} else {
				SquadComponent targetSquadComp = Components.SQUAD.get(targetSquad);
				
				if(targetSquadComp.members.size > 0){
					Entity targetUnit = targetSquadComp.members.get(index % targetSquadComp.members.size);
					Components.TARGET.get(unit).setTarget(targetUnit);
				}
			}
				
			

			Entity unit = (Entity)telegram.extraInfo;
			int index = squadComp.members.indexOf(unit, true);
			



			
			

			// Get the unit requesting a target and its index in our formation


			// Set the units target

			return true;
		}

		case TARGET_REMOVED:
			//Our target squad was just killed
			Components.FSM.get(entity).changeState(IDLE);
			return true;

			/** If the construction system has confirmed that it was able to create a new unit for us we need to tell that unit what
			 * we are currently doing and tell it to do the same */
		case CONSTRUCT_UNIT_CONFRIM:
			// If we are not currently idle we need to tell our new squad member to find a target
			if (Components.FSM.get(entity).getStateMachine().getCurrentState() != IDLE) {
				Entity unit = (Entity)telegram.extraInfo; // Get the new unit from the telegram
				Components.FSM.get(unit).changeState(UnitState.IDLE); // Tell our new member to be ready to accept targets
			}
			return true; // This message was handled

			// If we reach the default of the switch statement then the telegram has not been handled by this State
		default:
			return false;
		}
	}
}
