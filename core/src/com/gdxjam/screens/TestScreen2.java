package com.gdxjam.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.SteerableAdapter;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gdxjam.ai.states.UnitState;
import com.gdxjam.components.*;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.SteeringSystem;
import com.gdxjam.InputAITest;
import com.gdxjam.utils.CameraController;
import com.gdxjam.utils.GUItest;
import com.gdxjam.utils.generators.ResourceGenerator;

public class TestScreen2 extends AbstractScreen{

	
	private static final int PIXELS_PER_UNIT = 32;
	
	private PooledEngine engine;
	private PhysicsSystem physicsSystem;
    private Entity commander;
    private Vector2 comanderTarget;
    private GUItest guiTest;


    @Override
	public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();

		initEngine();
        guiTest = new GUItest(engine);
        inputMultiplexer.addProcessor(guiTest);
		createTestWorld();

        comanderTarget = new Vector2();
        inputMultiplexer.addProcessor(new InputAITest(engine.getSystem(CameraSystem.class).getCamera(), comanderTarget, commander));
        Gdx.input.setInputProcessor(inputMultiplexer);

	}
	
	public void createTestWorld(){
        createSquad(new Vector2(5,5));
        ResourceGenerator.generateForest(engine, new Vector2(10,10), 10, 5,0,0.1f);

	}
	
	public void createSquad(Vector2 position){
		position.set(position.x - 1, position.y - 1);
        Array<SteerableBodyComponent> agents = new Array<SteerableBodyComponent>();
        commander = createCommander(new Vector2(position.x, position.y));
        commander.add(engine.createComponent(CommanderComponent.class).init(new Array<Entity>()));
		for(int x = 0; x < 3; x++){
			for(int y = 0; y < 3; y++){
				if(x == 1 && y == 1){
                    Entity e  = commander;
                    SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(e);
                    agents.add(steerable);
                    Proximity<Vector2> proximity = new RadiusProximity<Vector2>(steerable,agents,8);
                    ProximityComponent proximityComponent = engine.createComponent(ProximityComponent.class).init(proximity);
                    e.add(proximityComponent);
                    Arrive<Vector2> arrive = new Arrive<Vector2>(steerable, new SteerableAdapter<Vector2>(){
                        @Override
                        public Vector2 getPosition() {
                            return comanderTarget;
                        }
                    }).setTimeToTarget(0.01f).setArrivalTolerance(0.002f).setDecelerationRadius(5f);

                    Components.STEERING_BEHAVIOR.get(e).setBehavior(arrive);

				}
				else{
					Entity e = createMember(new Vector2(position.x + x, position.y + y));
                    SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(e);
                    agents.add(steerable);
                    Proximity<Vector2> proximity = new RadiusProximity<Vector2>(steerable,agents,8);
                    ProximityComponent proximityComponent = engine.createComponent(ProximityComponent.class).init(proximity);
                    e.add(proximityComponent);
                    e.add(engine.createComponent(CommanderHolderComponent.class).init(commander));
                    e.add(engine.createComponent(StateMachineComponent.class).init(new StackStateMachine<Entity>(e, UnitState.COLLECT_RESOURCES)));
                    Components.COMMANDER.get(commander).addUnit(e);
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
        guiTest.act(delta);
        guiTest.draw();
	}
	
}
