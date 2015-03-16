package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
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
			if (squadComp.resourcesInRange.size > 0)
				Components.FSM.get(entity).changeState(HARVEST);
		}
	},
	
	HARVEST(){
		
	}
	
	;

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
		// TODO Auto-generated method stub
		return false;
	}

}
