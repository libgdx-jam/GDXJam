package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.gdxjam.GameWorld;

public class GameWorldSystem extends EntitySystem{
	
	private GameWorld world;
	private float gameTime = 0;
	
	public GameWorldSystem(GameWorld world){
		this.world = world;
	}
	
	public GameWorld getWorld(){
		return world;
	}

}
