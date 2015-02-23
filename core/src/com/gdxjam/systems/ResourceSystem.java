package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.IntMap;
import com.gdxjam.GameWorld;
import com.gdxjam.components.ResourceComponent.ResourceType;

public class ResourceSystem extends EntitySystem{
	
	public int population = 0;
	public IntMap<Integer> resources = new IntMap<Integer>();
	
	private GameWorld world;
	private HUDSystem hudSystem;
	
	public ResourceSystem(GameWorld world){
		this.world = world;
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		hudSystem = engine.getSystem(HUDSystem.class);
	}
	
	public void modifyResource(ResourceType type, int amount){
		int resource = resources.get(type.ordinal()) + amount;
		resources.put(type.ordinal(), resource);
		hudSystem.updateResource(type, resource);
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
