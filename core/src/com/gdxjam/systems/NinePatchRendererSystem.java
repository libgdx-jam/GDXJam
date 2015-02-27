package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.Components;
import com.gdxjam.components.NinePatchComponent;
import com.gdxjam.components.PhysicsComponent;

public class NinePatchRendererSystem extends IteratingSystem{
	
	private SpriteBatch batch;
	
	public NinePatchRendererSystem () {
		super(Family.all(NinePatchComponent.class).get());
	}

	
	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		NinePatchComponent patchComp = Components.NINE_PATCH.get(entity);
		
		if(Components.PHYSICS.has(entity)){
			PhysicsComponent physics =  Components.PHYSICS.get(entity);
			Vector2 pos = physics.body.getPosition();
	
			patchComp.patch.draw(batch, 0, 0, 1, 1);
		}
		
		
	}
	

}
