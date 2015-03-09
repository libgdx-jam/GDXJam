package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdxjam.components.DecayComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.utils.EntityUtils;

public class DecaySystem extends IteratingSystem{

	public DecaySystem () {
		super(Family.all(DecayComponent.class).get());
		
	}

	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		DecayComponent decayComp = Components.DECAY.get(entity);
		
		if(decayComp.elapsed >= decayComp.decayTime){
			EntityUtils.removeEntity(entity);
		} else{
			decayComp.elapsed += deltaTime;
		}
		
	}
	
}
