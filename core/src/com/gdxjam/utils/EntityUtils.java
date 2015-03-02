package com.gdxjam.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.StateMachineComponent;
import com.gdxjam.systems.GUISystem;

public class EntityUtils {
	
	private static PooledEngine engine;
	private static GUISystem guiSystem;
	
	public static void setEngine(PooledEngine engine){
		EntityUtils.engine = engine;
		EntityUtils.guiSystem = engine.getSystem(GUISystem.class);
	}
	
	public static void addToSquad(Entity entity, Entity squad){
		SquadComponent squadComp = Components.SQUAD.get(squad);
		
		entity.add(engine.createComponent(SquadMemberComponent.class));
		
		StateMachineComponent stateMachineComp = Components.STATE_MACHINE.get(entity);
		stateMachineComp.stateMachine.changeState(squadComp.state);
		squadComp.addMember(entity);
		guiSystem.updateSquad(squadComp);
	}
	
	public static void setSelectedSquadTarget(Vector2 target){
		ImmutableArray<Entity> squads = engine.getEntitiesFor(Family.all(SquadComponent.class).get());
		for(Entity entity : squads){
			SquadComponent squadComp = Components.SQUAD.get(entity);
			if(squadComp.selected){
				squadComp.setTarget(target);
			}
		}
	}
	
	public static void toggleSelectedSquad(int index){
		ImmutableArray<Entity> squads = engine.getEntitiesFor(Family.all(SquadComponent.class).get());
		for(Entity entity : squads){
			SquadComponent squadComp = Components.SQUAD.get(entity);
			if(squadComp.index == index){
				squadComp.selected = !squadComp.selected;
				guiSystem.setSelected(squadComp);
			}
		}
	}

}
