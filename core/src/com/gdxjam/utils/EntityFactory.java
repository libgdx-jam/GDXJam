package com.gdxjam.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.gdxjam.Assets;
import com.gdxjam.components.Components;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.PhysicsComponent;
import com.gdxjam.components.SpriteComponent;
import com.gdxjam.components.StateMachineComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.TargetFinderComponent;
import com.gdxjam.components.UnitComponent;
import com.gdxjam.systems.PhysicsSystem;

/**
 * 
 * @author Torin Wiebelt (Twiebs) Creates Ashley entities using a builder
 * 
 */

public class EntityFactory {

	private static final String TAG = "[" + EntityFactory.class.getSimpleName() +"]";
	
	private static PooledEngine engine;
	private static PhysicsSystem physicsSystem;

	public static void setEngine(PooledEngine engine) {
		EntityFactory.engine = engine;
		physicsSystem = engine.getSystem(PhysicsSystem.class);
	}

	public static Entity createOutpost(Vector2 position) {
		float radius = 12.0f;

		Entity entity = new EntityBuilder(position)
				.steerableBody(BodyType.StaticBody)
				.circleCollider(12.0f)
				.sprite(Assets.spacecraft.outpost, radius * 2, radius * 2)
				.health(1000)
				.addToEngine();
		return entity;
	}

	public static Entity createAsteroid(Vector2 position, float radius) {
		Entity entity = new EntityBuilder(position)
				.steerableBody(BodyType.KinematicBody)
				.circleCollider(radius)
				.health(50)
				.sprite(Assets.space.asteroid, radius * 2, radius * 2)
				.addToEngine();
		return entity;
	}

	public static Entity createUnit(Vector2 position) {
		float radius = 0.25f;
		Entity entity = new EntityBuilder(position)
				.steerableBody(BodyType.DynamicBody)
				.circleCollider(radius)
				.health(100)
				.sprite(Assets.spacecraft.ship, radius * 2, radius * 2)
				.getWithoutAdding();

		entity.add(engine.createComponent(SteeringBehaviorComponent.class));
		entity.add(engine.createComponent(StateMachineComponent.class).init(
				entity));
		entity.add(engine.createComponent(UnitComponent.class));

		// float range = 1.0f;
		// float arc = 90f;
		// Vector2 vertices[] = new Vector2[8];
		// for (int i = 0; i <= 7; i++) {
		// vertices[i] = new Vector2();
		// }
		//
		// vertices[0].set(0, 0);
		// for (int i = 0; i < 7; i++) {
		// float angle = (i / 6.0f * arc * MathUtils.degRad)
		// - (90 * MathUtils.degRad);
		// vertices[i + 1].set(range * MathUtils.cos(angle),
		// range * MathUtils.sin(angle));
		// }

		// PolygonShape poly = new PolygonShape();
		// poly.set(vertices);

		// FixtureDef sensorDef = new FixtureDef();
		// sensorDef.shape = poly;
		// sensorDef.isSensor = true;
		// body.createFixture(sensorDef);
		// poly.dispose();
		
		engine.addEntity(entity);
		return entity;
	}

	public static class EntityBuilder {
		private static final BodyType DEFAULT_BODY = BodyType.DynamicBody;

		public Vector2 position;
		public Entity entity;

		public EntityBuilder(Vector2 position) {
			this.position = position;
			entity = engine.createEntity();
		}

		public EntityBuilder steerableBody(BodyType type) {
			BodyDef def = new BodyDef();
			def.type = type;
			def.position.set(position);
			Body body = physicsSystem.createBody(def);

			PhysicsComponent physics = engine.createComponent(
					SteerableBodyComponent.class).init(body);
			entity.add(physics);
			return this;
		}

		public EntityBuilder circleCollider(float radius) {
			CircleShape shape = new CircleShape();
			shape.setRadius(radius);
			SteerableBodyComponent physics = Components.STEERABLE_BODY.get(entity);
			if (physics == null) {
				steerableBody(DEFAULT_BODY);
			}

			physics.body.createFixture(shape, 1.0f);
			return this;
		}

		public EntityBuilder health(int value) {
			HealthComponent health = engine
					.createComponent(HealthComponent.class);
			health.max = value;
			health.value = value;
			entity.add(health);
			return this;
			}
		
		public EntityBuilder targetFinder(float range){
			CircleShape shape = new CircleShape();
			shape.setRadius(range);
			FixtureDef def = new FixtureDef();
			def.isSensor = true;
			def.shape = shape;
			
			SteerableBodyComponent physics = Components.STEERABLE_BODY.get(entity);
			if(physics == null){
				Gdx.app.error(TAG, "can not add target finder to entity without a body");
				return this;
			}
			Fixture fixture = physics.body.createFixture(def);
			
			TargetFinderComponent targetFinder = engine.createComponent(TargetFinderComponent.class);
			fixture.setUserData(targetFinder);
			
			entity.add(targetFinder);
			return this;
		}
		

		public EntityBuilder sprite(TextureRegion region, float width,
				float height) {
			SpriteComponent spriteComp = engine.createComponent(
					SpriteComponent.class).init(region, position.x, position.y,
					width, height);
			entity.add(spriteComp);
			return this;
		}

		public Entity addToEngine() {
			engine.addEntity(entity);
			return entity;
		}
		
		public Entity getWithoutAdding(){
			return entity;
		}

	}
	
	@Deprecated
	public static Entity createEntity(Sprite sprite, Shape shape) {
		Entity entity = engine.createEntity();

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;
		def.position.set(sprite.getX(), sprite.getY());
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);

		body.createFixture(shape, 1.0f);
		shape.dispose();

		entity.add(engine.createComponent(SteerableBodyComponent.class).init(
				body));

		entity.add(engine.createComponent(HealthComponent.class));

		entity.add(engine.createComponent(SpriteComponent.class).init(sprite));

		engine.addEntity(entity);
		return entity;
	}

	@Deprecated
	public static Entity createCircleEntity(Sprite sprite) {
		CircleShape shape = new CircleShape();
		shape.setRadius(sprite.getWidth() / 2);
		return createEntity(sprite, shape);
	}

}
