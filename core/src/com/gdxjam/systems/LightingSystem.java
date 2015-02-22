package com.gdxjam.systems;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;

public class LightingSystem extends EntitySystem{
	private static final String TAG = "[" + LightingSystem.class.getSimpleName() + "]";
	
	private static final Color DAY = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	private static final Color NIGHT = new Color(0.0f, 0.0f, 0.0f, 0.4f);
	private static final Color ambient = NIGHT.cpy();
	
	private RayHandler rayHandler;

	public void init(World world){
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(ambient);
	}
	
	public void sunrise(float scale){
		blendLighting(NIGHT, DAY, scale);
	}
	
	public void sunset(float scale){
		blendLighting(DAY, NIGHT, scale);
	}
	
	public void blendLighting(Color base, Color target, float scale){
		float r = (target.r - base.r) * scale;
		float g = (target.g - base.g) * scale;
		float b = (target.b - base.b) * scale;
		float a = (target.a - base.a) * scale;
		ambient.add(r, g, b, a);
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
