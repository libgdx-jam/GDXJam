package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;

public class ResourceSystem extends EntitySystem{
	
	public int population = 0;
	public int resources = 500;
	private HUDSystem hudSystem;
	
	public ResourceSystem(){
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		hudSystem = engine.getSystem(HUDSystem.class);
		hudSystem.updateResource(resources);
	}
	
	public void modifyResource(int amount){
		resources += amount;
		hudSystem.updateResource(resources);
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		
//		if(world.food >= foodThreshold){
//			EntityFactory.createUnit(new Vector2(MathUtils.random(0, 64), MathUtils.random(0, 36)));
//			world.food = 0;
//		}
		
		
	}


}
