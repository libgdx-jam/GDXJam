package com.gdxjam.systems;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;

public class LightingSystem extends EntitySystem{
	
	private RayHandler rayHandler;
	private static final Color DAY = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	private static final Color NIGHT = new Color(0.0f, 0.0f, 0.0f, 0.5f);
	private Color ambient = NIGHT.cpy();

	public void init(World world){
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(ambient);
	}
	
	public void sunrise(float progress){
		ambient.lerp(DAY, progress);
		System.out.println(ambient.a);
		rayHandler.setAmbientLight(ambient);
	}
	
	public void sunset(float progress){
		ambient.lerp(NIGHT, progress);
		System.out.println(ambient.a);
		rayHandler.setAmbientLight(ambient);
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		init(engine.getSystem(PhysicsSystem.class).getWorld());
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		if(rayHandler != null){
			rayHandler.updateAndRender();
		}
	}
	
}
