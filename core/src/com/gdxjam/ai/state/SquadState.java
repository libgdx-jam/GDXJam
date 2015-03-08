
package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.gdxjam.components.Components;
import com.gdxjam.components.FSMComponent;
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
				Components.FSM.get(member).changeState(UnitState.HARVEST_IDLE);
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
			} else{	//Our units should follow formation
				SquadComponent squadComp = Components.SQUAD.get(entity);
				
				for(Entity unit : squadComp.members){
					FSMComponent unitFSM = Components.FSM.get(unit);
					if(!unitFSM.getStateMachine().isInState(UnitState.FORMATION))
						unitFSM.changeState(UnitState.FORMATION);
				}
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
			SquadComponent squadComp = Components.SQUAD.get(entity);

			//Our units are now in a combat state and will query us for targets
			for (int i = 0; i < squadComp.members.size; i++) {
				Entity member = squadComp.members.get(i);
				Components.FSM.get(member).changeState(UnitState.COMBAT_IDLE);
			}
			
		}
		
		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			TargetFinderComponent targetFinderComp = Components.TARGET_FINDER.get(entity);

			switch (telegram.message) {

			case Messages.requestTarget:
				Entity enemySquad = Components.TARGET.get(entity).getTarget();

				
				if(enemySquad != null){
					SquadComponent enemySquadComp = Components.SQUAD.get(enemySquad);
					if(enemySquadComp.members.size == 0){
						targetFinderComp.squads.removeValue(enemySquad, true);
						
						if(targetFinderComp.squads.size == 0){
							Components.FSM.get(entity).changeState(COMBAT_IDLE);
							return true;
						} else{
							Components.TARGET.get(entity).setTarget(targetFinderComp.squads.random());
						}
					}
					
					//Get the unit
					Entity unit = (Entity)telegram.extraInfo;
					SquadComponent squadComp = Components.SQUAD.get(entity);
					int index = squadComp.members.indexOf(unit, true);
					
					
					//Set the units target
					Entity unitTarget = enemySquadComp.members.get(index % enemySquadComp.members.size);
					Components.TARGET.get(unit).setTarget(unitTarget);
				}
				

				return true;
				
			case Messages.targetDestroyed:
				Gdx.app.error(TAG, "Target Destroyed!");
				Entity enemyUnit = (Entity)telegram.extraInfo;
				Entity squad = Components.SQUAD_MEMBER.get(enemyUnit).squad;
				SquadComponent squadComp = Components.SQUAD.get(squad);
				
				if(squadComp.members.size == 0){
					targetFinderComp.squads.removeValue(squad, true);
					if(targetFinderComp.squads.size == 0){
						Components.FSM.get(entity).changeState(COMBAT_IDLE);
					} else{
						Components.TARGET.get(entity).setTarget(targetFinderComp.squads.random());
					}
				}
				
				return true;

			default:
				return false;
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
			super.enter(entity);
			TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);

			// If we have targets available we don't need to be idle
			if (targetFinder.squads.size > 0) {
				TargetComponent targetComp = Components.TARGET.get(entity);
				targetComp.target = targetFinder.squads.random();
				Components.FSM.get(entity).changeState(COMBAT_ENGAGE);
			} else {	//Otherwise our units now just follow the formation if they are not already
				SquadComponent squadComp = Components.SQUAD.get(entity);
				
				for(Entity unit : squadComp.members){
					FSMComponent unitFSM = Components.FSM.get(unit);
					if(!unitFSM.getStateMachine().isInState(UnitState.FORMATION))
						unitFSM.changeState(UnitState.FORMATION);
				}
				
			}
		}
		
		public void getTarget(Entity entity){
			TargetFinderComponent targetFinder = Components.TARGET_FINDER.get(entity);
			TargetComponent targetComp = Components.TARGET.get(entity);
			targetComp.target = targetFinder.squads.random();
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			if (telegram.message == Messages.foundEnemy) {
				getTarget(entity);
				Components.FSM.get(entity).changeState(COMBAT_ENGAGE);
				return true;
			}
			return false;
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
