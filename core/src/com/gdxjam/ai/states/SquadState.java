package com.gdxjam.ai.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.StateMachineComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.components.TargetFinderComponent;

public enum SquadState implements State<Entity>{
	
	
	HARVEST_ENGAGE(){
		
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			SquadComponent squadComp = Components.SQUAD.get(entity);
			for(Entity member : squadComp.members){
				Components.STATE_MACHINE.get(member).stateMachine.changeState(UnitState.HARVEST);
			}
		}
		
	},
	
	HARVEST_IDLE() {
		
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);
			
			//If we have targets available we don't need to be idle
			if(targetFinder.resources.size > 0){
				Components.STATE_MACHINE.get(entity).stateMachine.changeState(HARVEST_ENGAGE);
			}
		}
		
		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			if(telegram.message == Messages.foundResource){
				Components.STATE_MACHINE.get(entity).stateMachine.changeState(HARVEST_ENGAGE);
				return true;
			}
			return false;
		}
		
	},
	
	COMBAT_ENGAGE(){
		@Override
		public void enter (Entity entity) {
			//When we enter this state we allready have a a target
			Entity target = Components.TARGET.get(entity).target;
			
			SquadComponent enemySquadComp = Components.SQUAD.get(target);
			SquadComponent squadComp = Components.SQUAD.get(entity);
			
			for(int i = 0; i < squadComp.members.size; i++){
//				int targetIndex = squadComp.members.size % enemySquadComp.members.size;
				Entity member = squadComp.members.get(i);
//				
//				TargetComponent memberTargetComp = Components.TARGET.get(member);
//				memberTargetComp.target = enemySquadComp.members.get(targetIndex);
//				
				StateMachineComponent stateComp = Components.STATE_MACHINE.get(member);
				stateComp.stateMachine.changeState(UnitState.FIND_TARGET);
			}
		}
		
		@Override
		public void update (Entity entity) {
			super.update(entity);
			//Make sure we still have a target
			//If we don't go back to idle
			if(Components.TARGET.get(entity).target == null)
				Components.STATE_MACHINE.get(entity).stateMachine.changeState(COMBAT_IDLE);
		}
		
		
	},
	
	COMBAT_IDLE(){
		
		@Override
		public void enter (Entity entity) {
			TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);
			
			//If we have targets available we don't need to be idle
			if(targetFinder.squads.size > 0){
				Components.STATE_MACHINE.get(entity).stateMachine.changeState(COMBAT_ENGAGE);
			}
		}
		
		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			if(telegram.message == Messages.foundEnemy){
				Components.STATE_MACHINE.get(entity).stateMachine.changeState(COMBAT_ENGAGE);
				return true;
			}
			return false;
		}
		
		@Override
		public void exit (Entity entity) {
			super.exit(entity);
			TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);
			if(targetFinder.squads.size == 0) return;
			
			TargetComponent targetComp = Components.TARGET.get(entity);
			if(targetFinder.squads.size > 1){	//If we have more than one avaible target find the closet
				//TODO squad targets based on distance
				//temp target finding gets random value
				targetComp.target = targetFinder.squads.random();
			} else{
				//Only one target to wory about... grab the first
				targetComp.target = targetFinder.squads.first();
			}
			
		}
		
		@Override
		public void update (Entity entity) {
			super.update(entity);
			TargetComponent targetComp = Components.TARGET.get(entity);
			
			if(targetComp.target == null){
				TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);
				Entity enemySquad  = targetFinder.squads.random();	//TODO find target by distance
				
				
				if(enemySquad != null){
					targetComp.target = enemySquad;
					


	
				}
			}
		}
		
		
		
	};

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
