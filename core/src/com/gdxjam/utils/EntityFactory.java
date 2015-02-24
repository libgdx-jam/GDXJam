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
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.LumberComponent;
import com.gdxjam.components.NinePatchComponent;
import com.gdxjam.components.PhysicsComponent;
import com.gdxjam.components.ProximityComponent;
import com.gdxjam.components.SpriteComponent;
import com.gdxjam.components.StateMachineComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.UnitComponent;
import com.gdxjam.systems.PhysicsSystem;

public class EntityFactory {

	private static PooledEngine engine;

	private static final float wallWidth = 0.75f;
	private static final float gateSize = 1.5f;
	private static final float towerSize = 2.5f;

	public static void setEngine(PooledEngine engine) {
		EntityFactory.engine = engine;
	}

	public static void createFortress(Vector2 position, float width,
			float height) {

		// Towers
		createTower(new Vector2(position.x - (width * 0.5f), position.y
				+ height * 0.5f));
		createTower(new Vector2(position.x + (width * 0.5f), position.y
				+ height * 0.5f));
		createTower(new Vector2(position.x - (width * 0.5f), position.y
				- height * 0.5f));
		createTower(new Vector2(position.x + (width * 0.5f), position.y
				- height * 0.5f));

		// Walls
		float horizontalSegmentSize = (width * 0.5f) - (gateSize * 0.5f)
				- (towerSize * 0.5f);
		float horizontalSegmentCenter = (-width * 0.5f)
				+ (horizontalSegmentSize * 0.5f) + (towerSize * 0.5f);
		float horizontalSegmentOffset = (height * 0.5f) + (wallWidth * 0.5f);

		float verticalSegmentSize = (height * 0.5f) - (gateSize * 0.5f)
				- (towerSize * 0.5f);
		float verticalSegmentCenter = (-height * 0.5f)
				+ (verticalSegmentSize * 0.5f) + (towerSize * 0.5f);
		float verticalSegmentOffset = (width * 0.5f) + (wallWidth * 0.5f);

		createWall(
				new Vector2(horizontalSegmentCenter, horizontalSegmentOffset)
						.add(position),
				horizontalSegmentSize, 0);
		createWall(new Vector2(-horizontalSegmentCenter,
				horizontalSegmentOffset).add(position), horizontalSegmentSize,
				0);
		createWall(new Vector2(horizontalSegmentCenter,
				-horizontalSegmentOffset).add(position), horizontalSegmentSize,
				0);
		createWall(new Vector2(-horizontalSegmentCenter,
				-horizontalSegmentOffset).add(position), horizontalSegmentSize,
				0);

		createWall(
				new Vector2(-verticalSegmentOffset, verticalSegmentCenter)
						.add(position),
				verticalSegmentSize, MathUtils.PI * 0.5f);
		createWall(
				new Vector2(-verticalSegmentOffset, -verticalSegmentCenter)
						.add(position),
				verticalSegmentSize, MathUtils.PI * 0.5f);
		createWall(
				new Vector2(verticalSegmentOffset, verticalSegmentCenter)
						.add(position),
				verticalSegmentSize, MathUtils.PI * 0.5f);
		createWall(
				new Vector2(verticalSegmentOffset, -verticalSegmentCenter)
						.add(position),
				verticalSegmentSize, MathUtils.PI * 0.5f);

	}

	public static Entity createWall(Vector2 center, float length, float rotation) {
		Entity entity = engine.createEntity();

		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(center);
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);

		FixtureDef fixdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(length * 0.5f, wallWidth * 0.5f, new Vector2(0, 0),
				rotation);
		fixdef.shape = shape;

		body.createFixture(fixdef);
		fixdef.shape.dispose();

		entity.add(engine.createComponent(PhysicsComponent.class).init(body));
		entity.add(engine.createComponent(HealthComponent.class));

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createTower(Vector2 position) {
		Entity entity = engine.createEntity();

		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(position);
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(towerSize * 0.5f, towerSize * 0.5f);

		body.createFixture(shape, 0.0f);

		entity.add(engine.createComponent(PhysicsComponent.class).init(body));

		entity.add(engine.createComponent(NinePatchComponent.class).init(
				Assets.getInstance().minimal.wall));

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createTree(Vector2 position, float radius, String type) {
		Entity entity = engine.createEntity();
		entity.add(engine.createComponent(LumberComponent.class).init(1));

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;
		def.position.set(position);
		def.angle = MathUtils.random(360);
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);

		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		body.createFixture(shape, 1.0f);
		shape.dispose();

		entity.add(engine.createComponent(SteerableBodyComponent.class).init(
				body));

		entity.add(engine.createComponent(HealthComponent.class));

		entity.add(engine.createComponent(SpriteComponent.class).init(
				Assets.getInstance().minimal.tree, position.x, position.y,
				radius * 2, radius * 2));

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createMothership(Vector2 position) {
		Entity entity = engine.createEntity();

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;
		def.position.set(position);
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);

		
		CircleShape shape = new CircleShape();
		float radius = 12;
		shape.setRadius(radius);
		body.createFixture(shape, 1.0f);
		shape.dispose();

		entity.add(engine.createComponent(SteerableBodyComponent.class).init(
				body));

		entity.add(engine.createComponent(HealthComponent.class));

		entity.add(engine.createComponent(SpriteComponent.class).init(
				Assets.getInstance().mothership.ship, position.x, position.y, radius,
				radius * 2));

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createOutpost(Vector2 position) {
		Entity entity = engine.createEntity();

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;
		def.position.set(position);
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);

		CircleShape shape = new CircleShape();
		float radius = 12;
		shape.setRadius(radius);
		body.createFixture(shape, 1.0f);
		shape.dispose();

		entity.add(engine.createComponent(SteerableBodyComponent.class).init(
				body));

		entity.add(engine.createComponent(HealthComponent.class));

		entity.add(engine.createComponent(SpriteComponent.class).init(
				Assets.spacecraft.outpost, position.x, position.y, radius * 2,
				radius * 2));

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createAsteroid(Vector2 position, float radius) {
		Entity entity = engine.createEntity();

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.KinematicBody;
		def.angularVelocity = MathUtils.random(0.0f, 4.0f);
		def.position.set(position);
		Body body = engine.getSystem(PhysicsSystem.class).createBody(def);

		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		body.createFixture(shape, 1.0f);
		shape.dispose();

		entity.add(engine.createComponent(SteerableBodyComponent.class).init(
				body));

		entity.add(engine.createComponent(HealthComponent.class));

		entity.add(engine.createComponent(SpriteComponent.class).init(
				Assets.getInstance().minimal.commander, position.x, position.y,
				radius * 2, radius * 2));

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createUnit(Vector2 position) {
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

		float range = 1.0f;
		float arc = 90f;
		Vector2 vertices[] = new Vector2[8];
		for (int i = 0; i <= 7; i++) {
			vertices[i] = new Vector2();
		}

		vertices[0].set(0, 0);
		for (int i = 0; i < 7; i++) {
			float angle = (i / 6.0f * arc * MathUtils.degRad)
					- (90 * MathUtils.degRad);
			vertices[i + 1].set(range * MathUtils.cos(angle),
					range * MathUtils.sin(angle));
		}

		PolygonShape poly = new PolygonShape();
		poly.set(vertices);

		FixtureDef sensorDef = new FixtureDef();
		sensorDef.shape = poly;
		sensorDef.isSensor = true;
		body.createFixture(sensorDef);
		poly.dispose();

		SteerableBodyComponent steerable = (SteerableBodyComponent) engine
				.createComponent(SteerableBodyComponent.class).init(body);
		entity.add(steerable);

		entity.add(engine.createComponent(SteeringBehaviorComponent.class));

		entity.add(engine.createComponent(StateMachineComponent.class).init(
				entity));

		entity.add(engine.createComponent(SpriteComponent.class).init(
				Assets.getInstance().minimal.unit, position.x, position.y,
				0.5f, 0.5f));

		entity.add(engine.createComponent(UnitComponent.class));

		entity.add(engine.createComponent(HealthComponent.class));

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createCommander(Vector2 position) {
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

		SteerableBodyComponent steerable = (SteerableBodyComponent) engine
				.createComponent(SteerableBodyComponent.class).init(body);
		entity.add(steerable);

		SteeringBehaviorComponent behaviorComp = engine
				.createComponent(SteeringBehaviorComponent.class);
		entity.add(behaviorComp);

		entity.add(engine.createComponent(SpriteComponent.class).init(
				Assets.getInstance().minimal.commander, position.x, position.y,
				0.5f, 0.5f));

		engine.addEntity(entity);
		return entity;
	}

	public static void createSquad(Vector2 position) {
		Entity commander = createCommander(position);
		Array<SteerableBodyComponent> units = new Array<SteerableBodyComponent>();
		commander.add(engine.createComponent(CommanderComponent.class).init(
				new Array<Entity>()));
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				Entity e;
				if (x == 0 && y == 0) {
					e = commander;
				} else {
					e = createUnit(position.cpy().add(x, y));
					e.add(engine
							.createComponent(CommanderHolderComponent.class)
							.init(commander));
					e.add(engine.createComponent(StateMachineComponent.class)
							.init(new StackStateMachine<Entity>(e,
									UnitState.COLLECT_RESOURCES)));
					Components.COMMANDER.get(commander).addUnit(e);
				}
				SteerableBodyComponent steerable = Components.STEERABLE_BODY
						.get(e);
				units.add(steerable);
				Proximity<Vector2> proximity = new RadiusProximity<Vector2>(
						steerable, units, 8);
				ProximityComponent proximityComponent = engine.createComponent(
						ProximityComponent.class).init(proximity);
				e.add(proximityComponent);
			}
		}
	}

}
