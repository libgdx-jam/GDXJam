
package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.components.TargetFinderComponent;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.Constants.BUILD;

public enum SquadState implements State<Entity> {

	HARVEST_ENGAGE() {

		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			SquadComponent squadComp = Components.SQUAD.get(entity);

			for (Entity member : squadComp.members) {
				Components.FSM.get(member).changeState(UnitState.FIND_RESOURCE);
			}
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			TargetFinderComponent targetFinderComp = Components.TARGET_FINDER.get(entity);

			switch (telegram.message) {

			case Messages.requestTarget:
				//We no longer have anything to do
				if(targetFinderComp.resources.size == 0){
					Components.FSM.get(entity).changeState(HARVEST_IDLE);
				}
				
				Entity unit = (Entity)telegram.extraInfo;
				SquadComponent squadComp = Components.SQUAD.get(entity);
				int index = squadComp.members.indexOf(unit, true);

				Entity target = targetFinderComp.resources.get(index % targetFinderComp.resources.size);
				
				TargetComponent targetComp = Components.TARGET.get(unit);
				targetComp.target = target;
				return true;
				
			case Messages.targetDestroyed:
				Gdx.app.error(TAG, "Target Destroyed!");
				Entity resource = (Entity)telegram.extraInfo;
				targetFinderComp.resourceAgents.removeValue(Components.STEERABLE.get(resource), true);
				targetFinderComp.resources.removeValue(resource, true);
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
			if (targetFinder.resources.size > 0) {
				Components.FSM.get(entity).changeState(HARVEST_ENGAGE);
			}
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			if (telegram.message == Messages.foundResource) {
				Components.FSM.get(entity).changeState(HARVEST_ENGAGE);
				return true;
			}
			return false;
		}

	},

	COMBAT_ENGAGE() {
		@Override
		public void enter (Entity entity) {
			// When we enter this state we allready have a a target
			Entity target = Components.TARGET.get(entity).target;

			SquadComponent enemySquadComp = Components.SQUAD.get(target);
			SquadComponent squadComp = Components.SQUAD.get(entity);

			for (int i = 0; i < squadComp.members.size; i++) {
// int targetIndex = squadComp.members.size % enemySquadComp.members.size;
				Entity member = squadComp.members.get(i);
//
// TargetComponent memberTargetComp = Components.TARGET.get(member);
// memberTargetComp.target = enemySquadComp.members.get(targetIndex);
//
				Components.FSM.get(member).changeState(UnitState.FIND_TARGET);
			}
		}

		@Override
		public void update (Entity entity) {
			super.update(entity);
			// Make sure we still have a target
			// If we don't go back to idle
			if (Components.TARGET.get(entity).target == null) Components.FSM.get(entity).changeState(COMBAT_IDLE);
		}

	},

	COMBAT_IDLE() {

		@Override
		public void enter (Entity entity) {
			TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);

			// If we have targets available we don't need to be idle
			if (targetFinder.squads.size > 0) {
				Components.FSM.get(entity).changeState(COMBAT_ENGAGE);
			}
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			if (telegram.message == Messages.foundEnemy) {
				Components.FSM.get(entity).changeState(COMBAT_ENGAGE);
				return true;
			}
			return false;
		}

		@Override
		public void exit (Entity entity) {
			super.exit(entity);
			TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);
			if (targetFinder.squads.size == 0) return;

			TargetComponent targetComp = Components.TARGET.get(entity);
			if (targetFinder.squads.size > 1) { // If we have more than one avaible target find the closet
				// TODO squad targets based on distance
				// temp target finding gets random value
				targetComp.target = targetFinder.squads.random();
			} else {
				// Only one target to wory about... grab the first
				targetComp.target = targetFinder.squads.first();
			}

		}

		@Override
		public void update (Entity entity) {
			super.update(entity);
			TargetComponent targetComp = Components.TARGET.get(entity);

			if (targetComp.target == null) {
				TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);
				Entity enemySquad = targetFinder.squads.random(); // TODO find target by distance

				if (enemySquad != null) {
					targetComp.target = enemySquad;

				}
			}
		}

	};

	private static final String TAG = SquadState.class.getSimpleName();

	@Override
	public void enter (Entity entity) {
		if (Constants.build == BUILD.DEV) {
			State<Entity> state = Components.FSM.get(entity).getStateMachine().getCurrentState();
			Gdx.app.debug(TAG, "Entered: " + state.toString());
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
