package com.gdxjam.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.gdxjam.components.Components;
import com.gdxjam.components.FactionComponent;
import com.gdxjam.systems.GUISystem;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityUtils;

public class SquadEntityListener implements EntityListener{
	
	private GUISystem guiSystem;
	
	public SquadEntityListener (GUISystem guiSystem) {
		this.guiSystem = guiSystem;
	}

	@Override
	public void entityAdded (Entity entity) {
		FactionComponent factionComp = Components.FACTION.get(entity);
		if(factionComp.faction == Constants.playerFaction)
			guiSystem.addSquad(entity);
	}

	@Override
	public void entityRemoved (Entity entity) {
		EntityUtils.clearTarget(entity);
		if(Components.FACTION.get(entity).faction == Constants.playerFaction)
			guiSystem.removeSquad(entity);
		
	}

}
