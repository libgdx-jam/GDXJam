package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.gdxjam.GameWorld;
import com.gdxjam.utils.Constants;

public class GameWorldSystem extends EntitySystem{
	
	private GameWorld world;
	private LightingSystem lightingSystem;

	public GameWorldSystem(GameWorld world){
		this.world = world;
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		lightingSystem = engine.getSystem(LightingSystem.class);
	}

	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		
		world.time += deltaTime;
		
		if(world.time > Constants.secondsPerHour * 24){
			world.time = 0;
		}
		if(lightingSystem != null){
			
			if(world.time > Constants.sunriseBegin && world.time < Constants.sunriseEnd){
				lightingSystem.sunrise(deltaTime * (1 / Constants.sunriseDuration));
			}
			
			else if(world.time > Constants.sunsetBegin && world.time < Constants.sunsetEnd){
				lightingSystem.sunset(deltaTime * (1 / Constants.sunsetDuration));
			}
			
		}
		
	}

	
	public GameWorld getWorld(){
		return world;
	}

}
