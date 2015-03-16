
package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.components.SquadComponent;
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
			SquadComponent squadComp = Components.SQUAD.get(entity); // We will be using this everywhere

			TelegramMessage telegramMsg = TelegramMessage.values()[telegram.message];
			switch (telegramMsg) {

			case REQUEST_TARGET:
				Entity unit = (Entity)telegram.extraInfo;
				int index = squadComp.members.indexOf(unit, true);

				// Get a target and set it
				Entity target = squadComp.resourcesInRange.get(index % squadComp.resourcesInRange.size);
				Components.TARGET.get(unit).setTarget(target);
				return true;

			case TARGET_DESTROYED:
				Entity resource = (Entity)telegram.extraInfo;

				squadComp.resourceAgents.removeValue(Components.STEERABLE.get(resource), true);
				squadComp.resourcesInRange.removeValue(resource, true);

				// We no longer have anything to do
				if (squadComp.resourcesInRange.size == 0) {
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

		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			TelegramMessage telegramMsg = TelegramMessage.values()[telegram.message];
			switch (telegramMsg) {

			case FOUND_RESOURCE:
				Components.FSM.get(entity).changeState(HARVEST);
				return true;

			default:
				return false;
			}
		}
	}

	;

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
