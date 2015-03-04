package com.gdxjam.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.ai.states.UnitState;
import com.gdxjam.components.Components;
import com.gdxjam.components.FactionComponent;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.StateMachineComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.systems.GUISystem;

public class EntityUtils {
	
	private static PooledEngine engine;
	private static GUISystem guiSystem;
	
	public static void setEngine(PooledEngine engine){
		EntityUtils.engine = engine;
		EntityUtils.guiSystem = engine.getSystem(GUISystem.class);
	}
	
	public static void removeEntity(Entity entity){
		engine.removeEntity(entity);
	}
	
	public static void clearTarget(Entity entity){
		ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(TargetComponent.class).get());
		for(Entity e : entities){
			TargetComponent targetComp = Components.TARGET.get(e);
			if(targetComp.target == entity){
				targetComp.target = null;
			}
			
		}
	}
	
	public static Entity findSquadWithoutFaction(Faction faction){
		ImmutableArray<Entity> squads = engine.getEntitiesFor(Family.all(SquadComponent.class, FactionComponent.class).get());
		for(Entity entity : squads){
			FactionComponent factionComp = Components.FACTION.get(entity);
			if(factionComp.faction != faction){
				return entity;
			}
		}
		return null;
	}
	
//	@Deprecated
//	public static void addToSquad(Entity entity, Entity squad){
//		FactionComponent entityFactionComp = Components.FACTION.get(entity);
//		FactionComponent squadFactionComp = Components.FACTION.get(entity);
//		
//		if(entityFactionComp.faction == squadFactionComp.faction){
//			SquadComponent squadComp = Components.SQUAD.get(squad);
//			
//			entity.add(engine.createComponent(SquadMemberComponent.class).init(squad));
//			
//			StateMachineComponent stateMachineComp = Components.STATE_MACHINE.get(entity);
//			stateMachineComp.stateMachine.changeState(UnitState.FORMATION);	//TODO set state based on squad state
//			squadComp.addMember(entity);
//			
//			if(squadFactionComp.faction == Faction.Player)
//				guiSystem.updateSquad(squad);
//		}
//	}
	
	public static void setSelectedSquadTarget(Vector2 target){
		ImmutableArray<Entity> squads = engine.getEntitiesFor(Family.all(SquadComponent.class).get());
		for(Entity entity : squads){
			SquadComponent squadComp = Components.SQUAD.get(entity);
			if(squadComp.selected){
				squadComp.setTarget(target);
			}
		}
	}
	

}
