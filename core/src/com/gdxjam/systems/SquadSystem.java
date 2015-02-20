package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.Squad;
import com.gdxjam.ai.states.SquadState;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.SteerableBodyComponent;

public class SquadSystem extends EntitySystem{
	
	public Array<Squad> squads;
	
	private PooledEngine engine;
	
	public SquadSystem(){
		squads = new Array<Squad>();
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		this.engine = (PooledEngine)engine;
	}
	
	public Squad createSquad(Entity commander){
		SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(commander);
		Squad squad = new Squad(steerable.getPosition().cpy());
		addSquadMember(commander, squad);
		squads.add(squad);
		return squad;
	}
	
	public Squad addSquadMember(Entity entity, Squad squad){
		squad.addMember(entity);
		
		entity.add(engine.createComponent(SquadMemberComponent.class).init(squad));
		Components.STATE_MACHINE.get(entity).stateMachine.changeState(SquadState.MOVE);
		return squad;
	}
	
	public Array<Squad> getSquads(){
		return squads;
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		if(squads.size > 0){
			squads.get(0);
		}
	}

}
