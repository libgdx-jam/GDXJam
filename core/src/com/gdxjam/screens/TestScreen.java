package com.gdxjam.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gdxjam.BattalionInputTest;
import com.gdxjam.ai.Battalion;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.SteeringSystem;

public class TestScreen extends AbstractScreen{

	
	private static final int PIXELS_PER_UNIT = 32;
	
	private PooledEngine engine;
	private PhysicsSystem physicsSystem;
	
	private Battalion battalionA;
	private Battalion battalionB;


	@Override
	public void show() {
		initEngine();
		createTestWorld();
		
		BattalionInputTest input = new BattalionInputTest(engine.getSystem(CameraSystem.class).getCamera(), battalionA, battalionB);
		Gdx.input.setInputProcessor(input);
	}
	
	public void createTestWorld(){
		Vector2 posA = new Vector2(5, 5);
		battalionA = new Battalion(posA);
		createSquad(posA, battalionA);
		
		Vector2 posB = new Vector2(15, 5);
		battalionB = new Battalion(posB);
		createSquad(posB, battalionB);
	}
	
	public void createSquad(Vector2 position, Battalion battalion){
		position.set(position.x - 1, position.y - 1);
		for(int x = 0; x < 3; x++){
			for(int y = 0; y < 3; y++){
				if(x == 1 && y == 1){
					battalion.addMember(createCommander(new Vector2(position.x + x, position.y + y)));
				}
				else{
					battalion.addMember(createMember(new Vector2(position.x + x, position.y + y)));
				}
			}
		}
	}
	
	public Entity createMember(Vector2 position){
		Entity entity = engine.createEntity();
		
		//Physics
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(position);
		def.linearDamping = 1.0f;
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);
		
		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.25f);
		fixDef.shape = shape;
		
		body.createFixture(fixDef);
		shape.dispose();
		
		SteerableBodyComponent steerable = (SteerableBodyComponent) engine.createComponent(SteerableBodyComponent.class).init(body);
		entity.add(steerable);
		
		entity.add(engine.createComponent(SteeringBehaviorComponent.class));
		
		engine.addEntity(entity);
		return entity;
	}
	
	public Entity createCommander(Vector2 position){
		Entity entity = engine.createEntity();
		
		//Physics
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(position);
		def.linearDamping = 1.0f;
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);
		
		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.25f);
		fixDef.shape = shape;
		
		body.createFixture(fixDef);
		shape.dispose();
		
		SteerableBodyComponent steerable = (SteerableBodyComponent) engine.createComponent(SteerableBodyComponent.class).init(body);
		entity.add(steerable);

		SteeringBehaviorComponent behaviorComp = engine.createComponent(SteeringBehaviorComponent.class);
		entity.add(behaviorComp);

		engine.addEntity(entity);
		return entity;
	}
	
	public void initEngine(){
		engine = new PooledEngine();
		OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth() / PIXELS_PER_UNIT,  Gdx.graphics.getHeight() / PIXELS_PER_UNIT);
		camera.position.set(10, 10, 0);
		engine.addSystem(new CameraSystem(camera));
		
		physicsSystem = new PhysicsSystem();
		engine.addSystem(physicsSystem);
		
		SteeringSystem steeringSystem = new SteeringSystem();
		engine.addSystem(steeringSystem);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		engine.update(delta);
	}
	
}
