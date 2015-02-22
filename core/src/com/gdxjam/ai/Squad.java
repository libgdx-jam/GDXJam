package com.gdxjam.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.states.SquadState;
import com.gdxjam.components.Components;
import com.gdxjam.components.SteerableBodyComponent;

public class Squad implements Telegraph{

	public int index = 0;
	public SteerableTarget target;
	public Array<Entity> entities;
	public Array<SteerableBodyComponent> agents;
	
	public State<Entity> state;
	
	public boolean selected = false;

	public Squad(Vector2 position, int index) {
		entities = new Array<Entity>();
		agents = new Array<SteerableBodyComponent>();
		target = new SteerableTarget(position, 1.0f);
		this.index = index;
		state = SquadState.MOVE;
	}

	
	public void addEntity(Entity entity){
		entities.add(entity);
		agents.add(Components.STEERABLE_BODY.get(entity));
	}

	public void setTarget(Vector2 target) {
		this.target.setPosition(target.x, target.y);
	}

	public void setTarget(float x, float y) {
		target.setPosition(x, y);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public SteerableTarget getTarget(){
		return target;
	}
	
	public Array<SteerableBodyComponent> getAgents(){
		return agents;
	}

	@Override
	public boolean handleMessage (Telegram msg) {
		return false;
	}
}
