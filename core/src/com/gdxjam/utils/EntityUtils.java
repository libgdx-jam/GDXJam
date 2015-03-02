package com.gdxjam.utils;

import com.badlogic.ashley.core.Entity;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;

public class EntityUtils {
	
	public static void addToSquad(Entity entity, Entity squad){
		SquadComponent squadComp = Components.SQUAD.get(squad);
		squadComp.addMember(entity);
	}

}
