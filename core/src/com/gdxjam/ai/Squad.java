package com.gdxjam.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.states.SquadState;
import com.gdxjam.components.Components;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.utils.Location2;

public class Squad implements Telegraph{

	public Formation<Vector2> formation;
	public FormationPattern<Vector2> pattern;
	public Location2 anchor;
	
	public int index = 0;
	public Array<Entity> entities;
	public Array<SteerableComponent> agents;
	
	public State<Entity> state;
	
	public boolean selected = false;

	public Squad(Vector2 position, int index) {
		entities = new Array<Entity>();
		agents = new Array<SteerableComponent>();
		this.index = index;
		this.anchor = new Location2(position);
		state = SquadState.MOVE;
	}
	
	public void addEntity(Entity entity){
		entities.add(entity);
		agents.add(Components.STEERABLE.get(entity));
	}

	public void setTarget(Vector2 target) {
		this.anchor.getPosition().set(target);
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public Array<SteerableComponent> getAgents(){
		return agents;
	}

	@Override
	public boolean handleMessage (Telegram msg) {
		return false;
	}
}
