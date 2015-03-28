package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdxjam.GameManager;
import com.gdxjam.components.DecayComponent;
import com.gdxjam.ecs.Components;

public class DecaySystem extends IteratingSystem{
	
	private PooledEngine engine;

	public DecaySystem () {
		super(Family.all(DecayComponent.class).get());
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		this.engine = (PooledEngine) engine;
	}

	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		DecayComponent decayComp = Components.DECAY.get(entity);
		
		if(decayComp.elapsed >= decayComp.decayTime)
			engine.removeEntity(entity);
		 else
			decayComp.elapsed += deltaTime;
	}
	
	@Override
	public boolean checkProcessing () {
		return !GameManager.isPaused();
	}
	
}
