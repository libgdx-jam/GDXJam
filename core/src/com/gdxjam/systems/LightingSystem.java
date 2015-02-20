package com.gdxjam.systems;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.gdxjam.GameWorld;

public class LightingSystem extends EntitySystem{
	
	private RayHandler rayHandler;
	private GameWorld world;
	private Color ambient = new Color(0.0f, 0.0f, 0.0f, 1.0f);

	public void init(World world){
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(ambient);
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		init(engine.getSystem(PhysicsSystem.class).getWorld());
		this.world = engine.getSystem(GameWorldSystem.class).getWorld();
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		if(rayHandler != null){
			rayHandler.updateAndRender();
		}
	}
	
}
