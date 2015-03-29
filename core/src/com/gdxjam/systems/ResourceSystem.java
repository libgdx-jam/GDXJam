package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdxjam.GameManager;
import com.gdxjam.components.DecayComponent;
import com.gdxjam.components.ResourceComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.systems.ParticleSystem.ParticleType;
import com.gdxjam.utils.EntityFactory;

public class ResourceSystem extends IteratingSystem{
	
	public int population = 0;
	public int resources = 500;
	private GUISystem guiSystem;
	private PooledEngine engine;
	
	public ResourceSystem(GUISystem guiSystem){
		super(Family.all(ResourceComponent.class).get());
		this.guiSystem = guiSystem;
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		this.engine = (PooledEngine) engine;
		//guiSystem = engine.getSystem(guiSystem.class);
		guiSystem.updateResource(resources);
	}
	
	public void modifyResource(int amount){
		resources += amount;
		guiSystem.updateResource(resources);
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);

	}

	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		ResourceComponent resourceComp = Components.RESOURCE.get(entity);
		
		if(resourceComp.value <= resourceComp.capactiy.min()){
			modifyResource((int)resourceComp.capactiy.max());
			
			EntityFactory.createParticle(Components.STEERABLE.get(entity).getPosition(), ParticleType.HARVSET);
			entity.remove(ResourceComponent.class);
			entity.add(engine.createComponent(DecayComponent.class).init(0.15f));
		}
		
	}
	
	@Override
	public boolean checkProcessing () {
		return !GameManager.isPaused();
	}


}
