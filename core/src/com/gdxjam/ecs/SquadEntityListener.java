package com.gdxjam.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.gdxjam.ai.state.TelegramMessage;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.systems.InputSystem;
import com.gdxjam.utils.Constants;

public class SquadEntityListener implements EntityListener{
	
	private InputSystem inputSystem;
	private PooledEngine engine;
	
	public SquadEntityListener (PooledEngine engine, InputSystem inputSystem) {
		this.engine = engine;
		this.inputSystem = inputSystem;
	}

	@Override
	public void entityAdded (Entity entity) {
		
	}

	@Override
	public void entityRemoved (Entity entity) {
		clearTarget(entity);
		if(Components.FACTION.get(entity).getFaction() == Constants.playerFaction)
			inputSystem.removeSquad(entity);
	}
	
	public void clearTarget (Entity target) {
		ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(TargetComponent.class, SquadComponent.class).get());
		
		for (Entity squad : entities) {
			SquadComponent squadComp = Components.SQUAD.get(squad);
			squadComp.untrack(squad, target);	//The squad no longer will track the target
			
			TargetComponent targetComp = Components.TARGET.get(squad);
			//If we were targeting that squad we remove it from our target
			if (targetComp.getTarget() == target) {
				targetComp.setTarget(null);

				//Dispatch a message to the entites FSM that there target was removed from the engine
				if (Components.FSM.has(squad)) {
					FSMComponent fsm = Components.FSM.get(squad);
					MessageManager.getInstance().dispatchMessage(null, fsm, TelegramMessage.TARGET_REMOVED.ordinal(), target);
				}
			}

		}
	}

}
