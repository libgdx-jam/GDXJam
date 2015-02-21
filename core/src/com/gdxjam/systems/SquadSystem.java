package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.Assets;
import com.gdxjam.ai.Squad;
import com.gdxjam.ai.states.SquadState;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.utils.Constants;

public class SquadSystem extends EntitySystem{
	
	public Array<Squad> squads;
	private PooledEngine engine;
	
	public SquadSystem(){
		squads = new Array<Squad>(true, Constants.maxSquads);
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		this.engine = (PooledEngine)engine;
	}
	
	public Squad createSquad(Entity commander){
		SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(commander);
		int index = squads.indexOf(null, true);
		Squad squad = new Squad(steerable.getPosition().cpy(), index);
		addSquadMember(commander, squad);
		Components.SPRITE.get(commander).sprite.setRegion(Assets.getManager().get("minimal.pack", TextureAtlas.class).findRegion("commander"));
		squads.add(squad);
		engine.getSystem(HUDSystem.class).addSquad(squad);
		
		return squad;
	}
	
	public void setTarget(Vector2 target){
		for (Squad squad : squads) {
			if (squad.isSelected()) {
				squad.setTarget(target);
			}
		}
	}
	
	public void setState(State<Entity> state){
		for(Squad squad : squads){
			if(squad.selected){
				setState(state, squad);
			}
		}
	}
	
	public void setState(State<Entity> state, Squad squad){
		for(Entity entity : squad.entities){
			Components.STATE_MACHINE.get(entity).stateMachine.changeState(state);
		}
	}
	
	public boolean toggleSelected(Squad squad){
		HUDSystem hudSystem = engine.getSystem(HUDSystem.class);
		
		squad.selected = !squad.selected;
		hudSystem.setSelected(squad);
		return squad.selected;
	}
	
	public boolean toggleSelected(int index){
		if(squads.size > index)
			return toggleSelected(squads.get(index));
		return false;
	}
	
	public Squad addSquadMember(Entity entity, Squad squad){
		squad.addEntity(entity);
		
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
