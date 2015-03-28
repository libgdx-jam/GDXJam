package com.gdxjam.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;

public class DebugEntityListener implements EntityListener{

	private static final String TAG = EntityManager.class.getSimpleName();
	
	@Override
	public void entityAdded (Entity entity) {
		if(Components.PARTICLE.has(entity)){
			Gdx.app.debug(TAG, "a particle was added to the engine");
		}
		
	}

	@Override
	public void entityRemoved (Entity entity) {
		if(Components.PARTICLE.has(entity)){
			Gdx.app.debug(TAG, "a particle was removed from the engine");
		}
		
	}
	
	

}
