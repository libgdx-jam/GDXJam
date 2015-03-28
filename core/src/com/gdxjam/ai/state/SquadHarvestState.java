package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.ecs.Components;

public enum SquadHarvestState implements State<Entity>{
	IDLE(){
		@Override
		public void enter (Entity entity) {
			SquadComponent squadComp = Components.SQUAD.get(entity);
		
			// If we have targets available we don't need to be idle
			if (squadComp.resourcesTracked.size > 0){
				Components.FSM.get(entity).changeState(HARVEST);
			} else {
				for (Entity member : squadComp.members) {
					if(!Components.FSM.get(member).getStateMachine().isInState(UnitState.IDLE))
						Components.FSM.get(member).changeState(UnitState.IDLE);
				}
				
			}
		}
		
		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
		TelegramMessage telegramMsg = TelegramMessage.values()[telegram.message];
		switch(telegramMsg){
		case DISCOVERED_RESOURCE:
			Components.FSM.get(entity).changeState(HARVEST);
			return true;
			
		default:
			return false;
			}
		}
	},
	
	HARVEST(){
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			
			SquadComponent squadComp = Components.SQUAD.get(entity);
			for (Entity member : squadComp.members) {
				Components.FSM.get(member).changeState(UnitState.FIND_TARGET);
			}
		}
		
		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			SquadComponent squadComp = Components.SQUAD.get(entity);
			TelegramMessage telegramMsg = TelegramMessage.values()[telegram.message];
			
			switch (telegramMsg) {
			case TARGET_REQUEST:
				//First we check if we have any target resources to delegate to our members
				if(squadComp.resourcesTracked.size <= 0){
					//There were no more targets
					Components.FSM.get(entity).changeState(SquadHarvestState.IDLE);
				} else {
					Entity unit = (Entity)telegram.extraInfo;
					int index = squadComp.members.indexOf(unit, true);

					// Get a target and set it
					Entity target = squadComp.resourcesTracked.get(index % squadComp.resourcesTracked.size);
					Components.TARGET.get(unit).setTarget(target);
				}
				return true;

			default:
				return false;
			}
		}
	}
	
	;

	@Override
	public void enter (Entity entity) {
//		Gdx.app.log("SquadState: ", Components.FSM.get(entity).getStateMachine().getCurrentState().toString());
		
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
