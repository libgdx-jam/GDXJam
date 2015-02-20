package com.gdxjam.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.components.Components;
import com.gdxjam.components.SteerableBodyComponent;

public class Squad implements Telegraph{

	public SteerableTarget target;
	public Array<Entity> members;
	public Array<SteerableBodyComponent> agents;
	private boolean selected = false;

	public Squad(Vector2 position) {
		members = new Array<Entity>();
		agents = new Array<SteerableBodyComponent>();
		target = new SteerableTarget(position, 1.0f);
	}

	
	public void addMember(Entity entity){
		members.add(entity);
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
