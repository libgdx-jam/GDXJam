package com.gdxjam.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.box2d.Body;
import com.gdxjam.systems.PhysicsSystem;

public class PhysicsEntityListener implements EntityListener{
	
	private PhysicsSystem physicsSystem;
	
	public PhysicsEntityListener (PhysicsSystem physicsSystem) {
		this.physicsSystem = physicsSystem;
	}

	@Override
	public void entityAdded (Entity entity) {
	}

	@Override
	public void entityRemoved (Entity entity) {
		Body body = Components.PHYSICS.get(entity).getBody();
		physicsSystem.destroyBody(body);
	}

}
