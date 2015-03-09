package com.gdxjam.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.gdxjam.components.Components;
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
		
		EntityUtils.clearTarget(entity);
		
		if(squadComp.members.size == 0){
			engine.removeEntity(unitComp.getSquad());
		}
		
	}
	


	
	
}
