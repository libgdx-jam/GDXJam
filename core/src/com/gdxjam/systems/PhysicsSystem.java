package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsSystem extends EntitySystem{
	
	private static final float TIME_STEP = 1.0f/60.f;
	private static final int VELOCITY_ITERATIONS = 8;
	private static final int POSITION_ITERATIONS = 8;
	
	private static World world;
	
	public PhysicsSystem(){
		world = new World(new Vector2(0, 0), true);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}
	
}
