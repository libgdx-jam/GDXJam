package com.gdxjam.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.gdxjam.components.FactionComponent;
import com.gdxjam.systems.InputSystem;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityUtils;

public class SquadEntityListener implements EntityListener{
	
	private InputSystem inputSystem;
	
	public SquadEntityListener (InputSystem inputSystem) {
		this.inputSystem = inputSystem;
	}

	@Override
	public void entityAdded (Entity entity) {
		
	}

	@Override
	public void entityRemoved (Entity entity) {
		EntityUtils.clearTarget(entity);
		if(Components.FACTION.get(entity).getFaction() == Constants.playerFaction)
			inputSystem.removeSquad(entity);
	}

}
