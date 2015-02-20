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
			if(isSunrise(world.time)){
				lightingSystem.sunrise((1 / (Constants.secondsPerHour * Constants.secondsPerMinute)) * deltaTime);
			}
			else if(isSunset(world.time)){
				lightingSystem.sunset((1 / (Constants.secondsPerHour * Constants.secondsPerMinute)) * deltaTime);
			}
			
		}
		
	}
	
	
	public boolean isSunrise(float time){
		float halfTransTime = Constants.transitionTime * 0.5f;
		return (time > Constants.dawn - halfTransTime && time < Constants.dawn + halfTransTime);
	}
	
	public boolean isSunset(float time){
		float halfTransTime = Constants.transitionTime * 0.5f;
		return (time > Constants.dusk - halfTransTime && time < Constants.dusk + halfTransTime);
	}
	
	public GameWorld getWorld(){
		return world;
	}

}
