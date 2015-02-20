package com.gdxjam.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.Assets;
import com.gdxjam.ai.states.UnitState;
import com.gdxjam.components.CommanderComponent;
import com.gdxjam.components.CommanderHolderComponent;
import com.gdxjam.components.Components;
import com.gdxjam.components.PhysicsComponent;
import com.gdxjam.components.ProximityComponent;
import com.gdxjam.components.SpriteComponent;
import com.gdxjam.components.StateMachineComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.systems.PhysicsSystem;

public class EntityFactory {
	
	private static PooledEngine engine;
	
	private static final float wallWidth = 0.75f;
	private static final float gateSize = 1.5f;
	private static final float towerSize = 2.5f;
	
	public static void setEngine(PooledEngine engine){
		EntityFactory.engine = engine;
	}
	
	public static Entity createFortress(Vector2 position, float width, float height){
		Entity entity = engine.createEntity();
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(position);
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);
		
		//Towers
		createTower(new Vector2(position.x - (width * 0.5f), position.y + height * 0.5f));
		createTower(new Vector2(position.x + (width * 0.5f), position.y + height * 0.5f));
		createTower(new Vector2(position.x - (width * 0.5f), position.y - height * 0.5f));
		createTower(new Vector2(position.x + (width * 0.5f), position.y - height * 0.5f));
		
		//Walls
		float horizontalSegmentSize = (width*0.5f) - (gateSize * 0.5f) - (towerSize * 0.5f);
		float horizontalSegmentCenter = (-width * 0.5f) + (horizontalSegmentSize * 0.5f) + (towerSize*0.5f);
		float horizontalSegmentOffset = (height * 0.5f) + (wallWidth * 0.5f);
		
		float verticalSegmentSize = (height*0.5f) - (gateSize * 0.5f) - (towerSize * 0.5f);
		float verticalSegmentCenter = (-height * 0.5f) + (verticalSegmentSize * 0.5f) + (towerSize * 0.5f);
		float verticalSegmentOffset = (width* 0.5f) + (wallWidth * 0.5f);
		
		FixtureDef topLeftWall = createWallFixture(horizontalSegmentSize, new Vector2(horizontalSegmentCenter, horizontalSegmentOffset), 0);
		FixtureDef topRightWall = createWallFixture(horizontalSegmentSize, new Vector2(-horizontalSegmentCenter, horizontalSegmentOffset), 0);
		FixtureDef bottomLeftWall = createWallFixture(horizontalSegmentSize,new Vector2(horizontalSegmentCenter, -horizontalSegmentOffset), 0);
		FixtureDef bottomRightWall = createWallFixture(horizontalSegmentSize, new Vector2(-horizontalSegmentCenter, -horizontalSegmentOffset), 0);
		
		FixtureDef leftTopWall = createWallFixture(verticalSegmentSize, new Vector2(-verticalSegmentOffset, verticalSegmentCenter), MathUtils.PI * 0.5f);
		FixtureDef leftBottomWall = createWallFixture(verticalSegmentSize, new Vector2(-verticalSegmentOffset, -verticalSegmentCenter), MathUtils.PI * 0.5f);
		FixtureDef rightTopWall = createWallFixture(verticalSegmentSize, new Vector2(verticalSegmentOffset, verticalSegmentCenter), MathUtils.PI * 0.5f);
		FixtureDef rightBottomWall = createWallFixture(verticalSegmentSize, new Vector2(verticalSegmentOffset, -verticalSegmentCenter), MathUtils.PI * 0.5f);
		
		body.createFixture(topLeftWall);
		topLeftWall.shape.dispose();
		body.createFixture(topRightWall);
		topRightWall.shape.dispose();
		
		body.createFixture(bottomLeftWall);
		bottomLeftWall.shape.dispose();
		body.createFixture(bottomRightWall);
		bottomRightWall.shape.dispose();
		
		body.createFixture(leftTopWall);
		leftTopWall.shape.dispose();
		body.createFixture(leftBottomWall);
		leftBottomWall.shape.dispose();
		
		body.createFixture(rightTopWall);
		rightTopWall.shape.dispose();
		body.createFixture(rightBottomWall);
		rightBottomWall.shape.dispose();
		
		entity.add(engine.createComponent(PhysicsComponent.class).init(body));
		
		engine.addEntity(entity);
		return entity;
	}
	
	public static Entity createTower(Vector2 position){
		Entity entity = engine.createEntity();
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(position);
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(towerSize * 0.5f, towerSize * 0.5f);
		
		body.createFixture(shape, 0.0f);
		
		entity.add(engine.createComponent(PhysicsComponent.class).init(body));
		
		engine.addEntity(entity);
		return entity;
	}
	
	public static FixtureDef createWallFixture(float length, Vector2 center, float rotation){
		FixtureDef def = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(length * 0.5f, wallWidth * 0.5f, center, rotation);
		def.shape = shape;
		return def;
	}
	
	public static Entity createUnit(Vector2 position){
		Entity entity = engine.createEntity();

		// Physics
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

		SteerableBodyComponent steerable = (SteerableBodyComponent)engine.createComponent(SteerableBodyComponent.class).init(body);
		entity.add(steerable);

		entity.add(engine.createComponent(SteeringBehaviorComponent.class));
		
		entity.add(engine.createComponent(StateMachineComponent.class).init(entity));
		
		entity.add(engine.createComponent(SpriteComponent.class)
			.init(Assets.getInstance().post.post1, position.x, position.y, 0.5f, 0.5f));

		engine.addEntity(entity);
		return entity;
	}
	
	public static Entity createCommander(Vector2 position){
		Entity entity = engine.createEntity();
		
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

		SteerableBodyComponent steerable = (SteerableBodyComponent)engine.createComponent(SteerableBodyComponent.class).init(body);
		entity.add(steerable);

		SteeringBehaviorComponent behaviorComp = engine.createComponent(SteeringBehaviorComponent.class);
		entity.add(behaviorComp);

		entity.add(engine.createComponent(SpriteComponent.class)
			.init(Assets.getInstance().chest.reg, position.x, position.y, 0.5f, 0.5f));

		engine.addEntity(entity);
		return entity;
	}

    public static void createSquad(Vector2 position){
        Entity commander = createCommander(position);
        Array<SteerableBodyComponent> units = new Array<SteerableBodyComponent>();
        commander.add(engine.createComponent(CommanderComponent.class).init(new Array<Entity>()));
        for(int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                Entity e;
                if(x==0 && y==0){
                    e = commander;
                }else{
                    e = createUnit(position.cpy().add(x,y));
                    e.add(engine.createComponent(CommanderHolderComponent.class).init(commander));
                    e.add(engine.createComponent(StateMachineComponent.class).init(new StackStateMachine<Entity>(e, UnitState.COLLECT_RESOURCES)));
                    Components.COMMANDER.get(commander).addUnit(e);
                }
                SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(e);
                units.add(steerable);
                Proximity<Vector2> proximity = new RadiusProximity<Vector2>(steerable,units,8);
                ProximityComponent proximityComponent = engine.createComponent(ProximityComponent.class).init(proximity);
                e.add(proximityComponent);
            }
        }
    }

}
