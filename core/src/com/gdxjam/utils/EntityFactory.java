package com.gdxjam.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdxjam.Assets;
import com.gdxjam.components.SpriteComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.systems.PhysicsSystem;

public class EntityFactory {
	
	private static PooledEngine engine;
	
	public static void setEngine(PooledEngine engine){
		EntityFactory.engine = engine;
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
		
		entity.add(engine.createComponent(SpriteComponent.class)
			.init(Assets.instance.post.post1, position.x, position.y, 0.5f, 0.5f));

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
			.init(Assets.instance.chest.reg, position.x, position.y, 0.5f, 0.5f));

		engine.addEntity(entity);
		return entity;
	}

}
