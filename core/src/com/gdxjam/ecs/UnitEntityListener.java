package com.gdxjam.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.UnitComponent;
import com.gdxjam.systems.GUISystem;
import com.gdxjam.utils.EntityUtils;

public class UnitEntityListener implements EntityListener{

	private PooledEngine engine;
	private GUISystem guiSystem;
	
	public UnitEntityListener (PooledEngine engine) {
		this.engine = engine;
		this.guiSystem = engine.getSystem(GUISystem.class);
	}
	
	@Override
	public void entityAdded (Entity entity) {
		UnitComponent unitComp = Components.UNIT.get(entity);
		guiSystem.updateSquad(unitComp.getSquad());
	}

	@Override
	public void entityRemoved (Entity entity) {
		UnitComponent unitComp = Components.UNIT.get(entity);
		SquadComponent squadComp = Components.SQUAD.get(unitComp.getSquad());
		
		squadComp.removeMember(entity);

		//First we check if the units squad has lost all its members
		//If so we remove it from all squads targets so when the unit trys to find a new target
		//The squad knows that its previous target is dead
		if(squadComp.members.size <= 0){
			Entity squad = unitComp.getSquad();
			EntityUtils.clearTarget(squad);
			engine.removeEntity(squad);
		}
		
		EntityUtils.clearTarget(entity);
		
	}
	


	
	
}
