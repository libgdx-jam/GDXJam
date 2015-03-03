package com.gdxjam.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.systems.GUISystem;

public class UnitEntityListener implements EntityListener{

	private PooledEngine engine;
	private GUISystem guiSystem;
	
	public UnitEntityListener (PooledEngine engine) {
		this.engine = engine;
		this.guiSystem = engine.getSystem(GUISystem.class);
	}
	
	@Override
	public void entityAdded (Entity entity) {
		SquadMemberComponent squadMemberComp = Components.SQUAD_MEMBER.get(entity);
		guiSystem.updateSquad(squadMemberComp.squad);
	}

	@Override
	public void entityRemoved (Entity entity) {
		SquadMemberComponent squadMemberComp = Components.SQUAD_MEMBER.get(entity);
		SquadComponent squadComp = Components.SQUAD.get(squadMemberComp.squad);
		squadComp.removeMember(entity);
		
		if(squadComp.members.size == 0){
			engine.removeEntity(squadMemberComp.squad);
		}
		
	}

	
	
}
