package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.Squad;
import com.gdxjam.components.Components;
import com.gdxjam.components.SteerableBodyComponent;

public class SquadSystem extends EntitySystem{
	
	public Array<Squad> squads;
	
	public SquadSystem(){
		squads = new Array<Squad>();
	}
	
	public Squad createSquad(Entity commander){
		SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(commander);
		Squad squad = new Squad(steerable.getPosition());
		squad.addMember(commander);
		return squad;
	}
	

}
