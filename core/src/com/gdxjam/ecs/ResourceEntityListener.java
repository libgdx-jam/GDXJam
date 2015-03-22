package com.gdxjam.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.gdxjam.utils.EntityUtils;

public class ResourceEntityListener implements EntityListener{

	@Override
	public void entityAdded (Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entityRemoved (Entity entity) {
		EntityUtils.clearTarget(entity);
		
	}
}
