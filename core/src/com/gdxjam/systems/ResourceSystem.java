package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.GameWorld;
import com.gdxjam.utils.EntityFactory;

public class ResourceSystem extends EntitySystem{
	
	public static final int foodThreshold = 10;
	
	private GameWorld world;
	
	public ResourceSystem(GameWorld world){
		this.world = world;
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		
		if(world.food >= foodThreshold){
			EntityFactory.createUnit(new Vector2(10, 10));
			world.food = 0;
		}
	}


}
