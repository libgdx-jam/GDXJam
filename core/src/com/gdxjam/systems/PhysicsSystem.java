package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class PhysicsSystem extends EntitySystem implements Disposable{
	
	private static final float TIME_STEP = 1.0f/60.f;
	private static final int VELOCITY_ITERATIONS = 8;
	private static final int POSITION_ITERATIONS = 8;
	
	public static boolean debug = true;
	
	private World world;
	private Box2DDebugRenderer renderer;
	private OrthographicCamera camera;

	public PhysicsSystem(){
		world = new World(new Vector2(0, 0), true);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.camera = engine.getSystem(CameraSystem.class).getCamera();
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		if(debug){
			drawDebug();
		}
	}
	
	public Body createBody(BodyDef def){
		return world.createBody(def);
	}
	
	public void destroyBody(Body body){
		world.destroyBody(body);
	}
	
	public World getWorld(){
		return world;
	}
	
	public void drawDebug(){
		if(renderer == null){
			renderer = new Box2DDebugRenderer();
		}
		renderer.render(world, camera.combined);
	}

	@Override
	public void dispose () {
		world.dispose();
	}
}
