package com.gdxjam.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gdxjam.Assets;
import com.gdxjam.components.Components;
import com.gdxjam.components.FactionComponent;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.ParalaxComponent;
import com.gdxjam.components.PhysicsComponent;
import com.gdxjam.components.ResourceComponent;
import com.gdxjam.components.SpriteComponent;
import com.gdxjam.components.StateMachineComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.TargetFinderComponent;
import com.gdxjam.systems.PhysicsSystem;

/**
 * 
 * @author Torin Wiebelt (Twiebs) Creates Ashley entities using a builder
 * 
 */

public class EntityFactory {

	private static final String TAG = "[" + EntityFactory.class.getSimpleName()
			+ "]";

	private static PooledEngine engine;
	private static PhysicsSystem physicsSystem;

	private static float mothershipRadius = Constants.mothershipRadius;
	private static float unitRadius = Constants.unitRadius;

	public static EntityBuilder buildEntity(Vector2 position) {
		return new EntityBuilder(position);
	}

	public static Entity createMothership(Vector2 position) {
		Entity entity = buildEntity(position)
				.physicsBody(BodyType.StaticBody)
				.circleCollider(mothershipRadius)
				.sprite(Assets.spacecraft.outpost, mothershipRadius * 2, mothershipRadius * 2)
				.health(1000)
				.addToEngine();
		return entity;
	}

	public static Entity createAsteroid(Vector2 position, float radius) {
		Entity entity = buildEntity(position)
				.physicsBody(BodyType.KinematicBody)
				.circleCollider(radius)
				.health(50)
				.resource(5)
				.sprite(Assets.space.asteroids.random(), radius * 2, radius * 2)
				.addToEngine();
		return entity;
	}

	public static Entity createUnit(Vector2 position, Faction faction) {
		Entity entity = buildEntity(position)
			   .physicsBody(BodyType.DynamicBody)
				.circleCollider(unitRadius)
				.damping(1, 0)
				.steerable()
				.health(100)
				.faction(faction)
				.sprite(faction == Faction.Player ? Assets.spacecraft.ship : Assets.spacecraft.enemy, unitRadius * 2, unitRadius * 2)
				.getWithoutAdding();

		entity.add(engine.createComponent(SteeringBehaviorComponent.class));
		entity.add(engine.createComponent(StateMachineComponent.class).init(entity));

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createBackgroundArt(Vector2 position, float width,
			float height, TextureRegion region, int layer) {
		Entity entity = buildEntity(position)
			.sprite(region, width, height)
			.getWithoutAdding();
		entity.add(engine.createComponent(ParalaxComponent.class).init(position.x, position.y, width, height, layer));

		// TODO add paralaxComponent to be processed by the paralaxSystem
		engine.addEntity(entity);
		return entity;
	}

	/**
	 * Called when the GameManager first initializes a new engine
	 * 
	 * @param engine
	 *            The engine that the factory will use to create its entities
	 */
	public static void setEngine(PooledEngine engine) {
		EntityFactory.engine = engine;
		physicsSystem = engine.getSystem(PhysicsSystem.class);
	}

	/**
	 * Creates an entity from the engine when first instantiated Exit builder by
	 * calling addToEngine() or getWithoutAdding()
	 * 
	 */

	public static class EntityBuilder {
		private static final BodyType DEFAULT_BODY = BodyType.DynamicBody;

		public Vector2 position;
		public Entity entity;

		public EntityBuilder(Vector2 position) {
			this.position = position;
			entity = engine.createEntity();
		}

		public EntityBuilder physicsBody(BodyType type) {
			BodyDef def = new BodyDef();
			def.type = type;
			def.position.set(position);
			Body body = physicsSystem.createBody(def);

			PhysicsComponent physics = engine.createComponent(
					PhysicsComponent.class).init(body);
			entity.add(physics);
			return this;
		}
		
		public EntityBuilder damping(float angular, float linear){
			if(Components.PHYSICS.has(entity)){
				PhysicsComponent physics = Components.PHYSICS.get(entity);
				physics.body.setAngularDamping(angular);
				physics.body.setLinearDamping(linear);
			} 
			else{
				Gdx.app.error(TAG, "entity is missing physics component!");
			}
			return this;
		}
		
		public EntityBuilder resource(int amount){
			ResourceComponent resourceComp = engine.createComponent(ResourceComponent.class);
			resourceComp.amount = amount;
			entity.add(resourceComp);
			
			return this;
		}
		
		public EntityBuilder faction(Faction faction){
			FactionComponent factionComp = engine.createComponent(FactionComponent.class);
			factionComp.faction = faction;
			
			entity.add(factionComp);
			return this;
		}

		public EntityBuilder steerable() {
			PhysicsComponent physics = Components.PHYSICS.get(entity);
			if (physics == null) {
				Gdx.app.error(TAG, "cannot create a steerable without physics!");
				return this;
			}
			SteerableComponent steerable = engine.createComponent(
					SteerableComponent.class).init(physics.body);
			entity.add(steerable);
			return this;
		}

		public EntityBuilder circleCollider(float radius) {
			CircleShape shape = new CircleShape();
			shape.setRadius(radius);
			PhysicsComponent physics = Components.PHYSICS.get(entity);
			if (physics == null) {
				physicsBody(DEFAULT_BODY);
			}

			physics.body.createFixture(shape, 1.0f);
			return this;
		}

		public EntityBuilder rangeSensor(float range, float arc) {
			Body body;
			if (Components.PHYSICS.has(entity)) {
				body = Components.PHYSICS.get(entity).body;
			} else {
				Gdx.app.error(TAG,
						"can not add range sensor : entity does not have a physics component!");
				return this;
			}

			Vector2 vertices[] = new Vector2[8];

			for (int i = 0; i <= 7; i++) {
				vertices[i] = new Vector2(0, 0);
			}

			for (int i = 0; i < 7; i++) {
				float angle = (i / 6.0f * arc * MathUtils.degRad)
						- (90 * MathUtils.degRad);
				vertices[i + 1].set(range * MathUtils.cos(angle), range
						* MathUtils.sin(angle));
			}

			PolygonShape poly = new PolygonShape();
			poly.set(vertices);

			FixtureDef sensorDef = new FixtureDef();
			sensorDef.shape = poly;
			sensorDef.isSensor = true;
			body.createFixture(sensorDef);
			poly.dispose();
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

		public EntityBuilder targetFinder(float range) {
			CircleShape shape = new CircleShape();
			shape.setRadius(range);
			FixtureDef def = new FixtureDef();
			def.isSensor = true;
			def.shape = shape;

			PhysicsComponent physics = Components.PHYSICS.get(entity);
			if (physics == null) {
				Gdx.app.error(TAG,
						"can not add target finder to entity without a body");
				return this;
			}
			Fixture fixture = physics.body.createFixture(def);

			TargetFinderComponent targetFinder = engine
					.createComponent(TargetFinderComponent.class);
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

		public Entity getWithoutAdding() {
			return entity;
		}

	}
}
