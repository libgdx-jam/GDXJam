
package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.TargetFinderComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.Constants.BUILD;

public enum SquadState implements State<Entity> {

	HARVEST() {

		@Override
		public void enter (Entity entity) {
			super.enter(entity);

			// Tell our squad members that they should be ready to harvest
			SquadComponent squadComp = Components.SQUAD.get(entity);
			for (Entity member : squadComp.members) {
				Components.FSM.get(member).changeState(UnitState.HARVEST_IDLE);
			}
		}

		@Override
		public void exit (Entity entity) {
			super.exit(entity);

			SquadComponent squadComp = Components.SQUAD.get(entity);
			for (Entity member : squadComp.members) {
				Components.FSM.get(member).changeState(UnitState.FORMATION);
			}
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			TargetFinderComponent targetFinderComp = Components.TARGET_FINDER.get(entity);

			switch (telegram.message) {

			case Messages.requestTarget:
				Entity unit = (Entity)telegram.extraInfo;
				SquadComponent squadComp = Components.SQUAD.get(entity);
				int index = squadComp.members.indexOf(unit, true);

				// Get a target and set it
				Entity target = targetFinderComp.resources.get(index % targetFinderComp.resources.size);
				Components.TARGET.get(unit).setTarget(target);
				return true;

			case Messages.targetDestroyed:
				Gdx.app.error(TAG, "Target Destroyed!");
				Entity resource = (Entity)telegram.extraInfo;
				targetFinderComp.resourceAgents.removeValue(Components.STEERABLE.get(resource), true);
				targetFinderComp.resources.removeValue(resource, true);

				// We no longer have anything to do
				if (targetFinderComp.resources.size == 0) {
					Components.FSM.get(entity).changeState(HARVEST_IDLE);
				}

				return true;

			default:
				return false;
			}
		}

	},

	HARVEST_IDLE() {

		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);

			// If we have targets available we don't need to be idle
			if (targetFinder.resources.size > 0)
				Components.FSM.get(entity).changeState(HARVEST);
			else { // Our units should follow formation
				SquadComponent squadComp = Components.SQUAD.get(entity);

				// TODO is this unnecessary?
				for (Entity unit : squadComp.members) {
					FSMComponent unitFSM = Components.FSM.get(unit);
					if (!unitFSM.getStateMachine().isInState(UnitState.FORMATION)) unitFSM.changeState(UnitState.FORMATION);
				}

			}
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			switch (telegram.message) {

			case Messages.foundResource:
				Components.FSM.get(entity).changeState(HARVEST);
				return true;

			default:
				return false;
			}
		}
	},

	COMBAT() {

		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			SquadComponent squadComp = Components.SQUAD.get(entity);

			// Our units are now in a combat state and will query us for targets
			for (int i = 0; i < squadComp.members.size; i++) {
				Entity member = squadComp.members.get(i);
				Components.FSM.get(member).changeState(UnitState.COMBAT_IDLE);
			}

			// If were not the player lets do some cool stuff
			if (Components.FACTION.get(entity).getFaction() != Constants.playerFaction) {
				Components.FSM.get(entity).changeState(AICombatState.AGRESSIVE);
			}

		}
		
		@Override
		public void update (Entity entity) {
			if(Components.FACTION.get(entity).getFaction() != Constants.playerFaction){
				Entity targetSquad = Components.TARGET.get(entity).getTarget();
				SteerableComponent targetSteerable = Components.STEERABLE.get(targetSquad);
				SteerableComponent steerable = Components.STEERABLE.get(entity);
				
			}
			super.update(entity);
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			TargetFinderComponent targetFinderComp = Components.TARGET_FINDER.get(entity);

			switch (telegram.message) {

			case Messages.requestTarget:
				Entity enemySquad = Components.TARGET.get(entity).getTarget();

				if (enemySquad != null) { // This should be pointless
					SquadComponent enemySquadComp = Components.SQUAD.get(enemySquad);
					if (enemySquadComp.members.size == 0) {
						targetFinderComp.squads.removeValue(enemySquad, true);

						if (targetFinderComp.squads.size == 0) {
							Components.FSM.get(entity).changeState(COMBAT_IDLE);
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
				Gdx.app.error(TAG, "Target Destroyed!");
				Entity enemyUnit = (Entity)telegram.extraInfo;
				Entity squad = Components.UNIT.get(enemyUnit).getSquad();
				SquadComponent squadComp = Components.SQUAD.get(squad);

				if (squadComp.members.size == 0) {
					targetFinderComp.squads.removeValue(squad, true);
					if (targetFinderComp.squads.size == 0) {
						Components.FSM.get(entity).changeState(COMBAT_IDLE);
					} else {
						Components.TARGET.get(entity).setTarget(targetFinderComp.squads.random());
					}
				}

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
			TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);

			// If we have targets available we don't need to be idle
			if (targetFinder.squads.size > 0) {
				Components.TARGET.get(entity).setTarget(targetFinder.squads.random());
				Components.FSM.get(entity).changeState(COMBAT);
			}
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			switch (telegram.message) {

			case Messages.foundEnemy:
				Gdx.app.debug(TAG, "Found enemy!");
				TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);
				Components.TARGET.get(entity).setTarget(targetFinder.squads.random());
				Components.FSM.get(entity).changeState(COMBAT);
				return true;

			default:
				return false;
			}
		}

	};

	private static final String TAG = SquadState.class.getSimpleName();

	@Override
	public void enter (Entity entity) {
		if (Constants.build == BUILD.DEV) {
			if (Components.FACTION.get(entity).getFaction() == Constants.playerFaction) {
				State<Entity> state = Components.FSM.get(entity).getStateMachine().getCurrentState();
				Gdx.app.debug(TAG, "Entered: " + state.toString());
			}
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
		// TODO Auto-generated method stub
		return false;
	}

}
