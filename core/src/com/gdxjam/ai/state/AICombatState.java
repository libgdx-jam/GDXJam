
package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.TargetFinderComponent;
import com.gdxjam.ecs.Components;

public enum AICombatState implements State<Entity> {

	AGRESSIVE() {
		@Override
		public void enter (Entity entity) {
			SteerableComponent steerable = Components.STEERABLE.get(entity);
			Entity targetSquad = Components.TARGET.get(entity).getTarget();
			SteerableComponent targetSteerable = Components.STEERABLE.get(targetSquad);

			Pursue<Vector2> pursueSB = new Pursue<Vector2>(steerable, targetSteerable).setMaxPredictionTime(0.1f);

			Components.STEERING_BEHAVIOR.get(entity).setBehavior(pursueSB);
		}

		@Override
		public void update (Entity entity) {
			float distance = distanceToTarget(entity);

			if (distance <= SquadTatics.DEFENSIVE_RADIUS) Components.FSM.get(entity).changeState(DEFENSIVE);
		}

	},

	DEFENSIVE() {
		@Override
		public void enter (Entity entity) {
			Entity targetSquad = Components.TARGET.get(entity).getTarget();
			SteerableComponent targetSteerable = Components.STEERABLE.get(targetSquad);
			SteerableComponent steerable = Components.STEERABLE.get(entity);

			Evade<Vector2> evadeSB = new Evade<Vector2>(steerable, targetSteerable, 0.1f);

			Components.STEERING_BEHAVIOR.get(entity).setBehavior(evadeSB);
		}

		@Override
		public void update (Entity entity) {
			float distance = distanceToTarget(entity);

			if (distance >= SquadTatics.AGRESSIVE_RADIUS) {
				Components.FSM.get(entity).changeState(AGRESSIVE);
			}
		}
	}

	;

	public float distanceToTarget (Entity entity) {
		Entity targetSquad = Components.TARGET.get(entity).getTarget();
		SteerableComponent targetSteerable = Components.STEERABLE.get(targetSquad);
		SteerableComponent steerable = Components.STEERABLE.get(entity);

		return targetSteerable.getPosition().dst(steerable.getPosition());
	}

	@Override
	public void enter (Entity entity) {
		// TODO Auto-generated method stub

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
		TargetFinderComponent targetFinderComp = Components.TARGET_FINDER.get(entity);

		switch (telegram.message) {

		/** If we have found a new target we need to assess its threat level to decide if we need to switch targets. */
		case Messages.foundEnemy:
			//TODO assess threat level of new targets
			return true;
			
		case Messages.requestTarget:
			Entity enemySquad = Components.TARGET.get(entity).getTarget();

			if (enemySquad != null) { // This should be pointless
				SquadComponent enemySquadComp = Components.SQUAD.get(enemySquad);
				if (enemySquadComp.members.size == 0) {
					targetFinderComp.squads.removeValue(enemySquad, true);

					if (targetFinderComp.squads.size == 0) {
						Components.FSM.get(entity).changeState(SquadState.COMBAT_IDLE);
						return true;
					} else {
						Components.TARGET.get(entity).setTarget(targetFinderComp.squads.random());
					}
				}

				// Get the unit
				Entity unit = (Entity)telegram.extraInfo;
				SquadComponent squadComp = Components.SQUAD.get(entity);
				int index = squadComp.members.indexOf(unit, true);

				// Set the units target
				Entity unitTarget = enemySquadComp.members.get(index % enemySquadComp.members.size);
				Components.TARGET.get(unit).setTarget(unitTarget);
			}

			return true;

		case Messages.targetDestroyed:
			Entity enemyUnit = (Entity)telegram.extraInfo;
			Entity squad = Components.UNIT.get(enemyUnit).getSquad();
			SquadComponent squadComp = Components.SQUAD.get(squad);

			if (squadComp.members.size == 0) {
				targetFinderComp.squads.removeValue(squad, true);
				if (targetFinderComp.squads.size == 0) {
					Components.FSM.get(entity).changeState(SquadState.COMBAT_IDLE);
				} else {
					Components.TARGET.get(entity).setTarget(targetFinderComp.squads.random());
				}
			}

			return true;

		default:
			return false;
		}
	}

}
