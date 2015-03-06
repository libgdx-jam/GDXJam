package com.gdxjam.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gdxjam.Assets;
import com.gdxjam.ai.states.UnitState;
import com.gdxjam.components.Components;
import com.gdxjam.components.DecayComponent;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.components.FactionComponent;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.MothershipComponent;
import com.gdxjam.components.ParalaxComponent;
import com.gdxjam.components.ParticleComponent;
import com.gdxjam.components.PhysicsComponent;
import com.gdxjam.components.ProjectileComponent;
import com.gdxjam.components.ResourceComponent;
import com.gdxjam.components.SpriteComponent;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.components.TargetFinderComponent;
import com.gdxjam.components.WeaponComponent;
import com.gdxjam.ecs.EntityCategory;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.utils.EntityFactory.PhysicsBuilder.FixtureBuilder;

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
	private static EntityBuilder entityBuilder;
	private static PhysicsBuilder physicsBuilder = new PhysicsBuilder();
	private static FixtureBuilder fixtureBuilder = new FixtureBuilder();

	public static EntityBuilder buildEntity(Vector2 position) {
		if (entityBuilder == null) {
			entityBuilder = new EntityBuilder();
		}
		return entityBuilder.reset(position);
	}

	public static Entity createMothership(Vector2 position) {
		Entity entity = buildEntity(position)
				.physicsBody(BodyType.StaticBody)
				.circleCollider(Constants.mothershipRadius, 1.0f)
				.sprite(Assets.spacecraft.motherships.get(Constants.playerFaction
						.ordinal()), Constants.mothershipRadius * 2,
						Constants.mothershipRadius * 2)
				.faction(Constants.playerFaction).mothership().health(1000)
				.addToEngine();
		return entity;
	}

	public static Entity createAsteroid(Vector2 position, float radius) {
		Entity entity = buildEntity(position)
				.physicsBody(BodyType.StaticBody)
				.circleCollider(radius, 50.0f)
				.resource(5)
				.faction(Faction.NONE)
				.sprite(Assets.space.asteroids.random(), radius * 2, radius * 2)
				.addToEngine();
		return entity;
	}

	public static Entity createUnit(Entity squad) {
		Vector2 squadPos = Components.STEERABLE.get(squad).getPosition();
		Vector2 position = new Vector2(128, 128); // TODO dependant on world
													// size
		float angle = squadPos.angleRad(position);
		position.add(MathUtils.cos(angle) + (Constants.unitRadius * 2),
				MathUtils.sin(angle) + (Constants.unitRadius * 2));
		return createUnit(position, squad);
	}

	public static Entity createUnit(Vector2 position, Entity squad) {
		SquadComponent squadComp = Components.SQUAD.get(squad);
		Faction faction = Components.FACTION.get(squad).faction;

		Entity entity = buildEntity(position)
				.physicsBody(BodyType.DynamicBody)
				.circleCollider(Constants.unitRadius, 1.0f)
				.damping(1, 0)
				.steerable()
				.steeringBehavior()
				.health(100)
				.faction(faction)
				.target()
				.weapon(20, 1.0f)
				.sprite(Assets.spacecraft.ships.get(faction.ordinal()),
						Constants.unitRadius * 2, Constants.unitRadius * 2)
				.getWithoutAdding();

		PhysicsComponent physicsComp = Components.PHYSICS.get(entity);
		SquadMemberComponent squadMemberComp = engine.createComponent(
				SquadMemberComponent.class).init(squad, physicsComp.body);
		entity.add(squadMemberComp);
		squadComp.addMember(entity);

		Components.STEERABLE.get(entity).setIndependentFacing(true);
		FSMComponent stateMachineComponent = engine.createComponent(
				FSMComponent.class).init(entity);
		stateMachineComponent.changeState(UnitState.FORMATION);

		entity.add(stateMachineComponent);

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createSquad(Vector2 position, Faction faction) {
		Entity entity = buildEntity(position)
				.physicsBody(BodyType.DynamicBody)
				.circleSensor(30.0f)
				.faction(faction)
				.target()
				.filter(EntityCategory.SQUAD, 0,
						EntityCategory.SQUAD | EntityCategory.RESOURCE)
				.steeringBehavior().stateMachine().getWithoutAdding();

		SteerableComponent steerable = engine.createComponent(
				SteerableComponent.class).init(
				Components.PHYSICS.get(entity).body);
		SquadComponent squadComp = engine.createComponent(SquadComponent.class)
				.init(steerable);
		squadComp.targetLocation.getPosition().set(position);

		// A good rule of thumb is to make the maximum speed of the formation
		// around
		// half that of the members. We also give the anchor point far less
		// acceleration.
		steerable.setMaxLinearSpeed(SteerableComponent.MAX_LINEAR_SPEED / 2);
		steerable
				.setMaxLinearAcceleration(SteerableComponent.MAX_LINEAR_ACCELERATION / 10);

		Arrive<Vector2> arriveSB = new Arrive<Vector2>(steerable)
				.setTarget(squadComp.targetLocation).setTimeToTarget(0.001f)
				.setDecelerationRadius(2f).setArrivalTolerance(0.0001f);
		SteeringBehavior<Vector2> sb = arriveSB;

		if (steerable.isIndependentFacing()) {
			LookWhereYouAreGoing<Vector2> lookWhereYouAreGoingSB = new LookWhereYouAreGoing<Vector2>(
					steerable) //
					.setTimeToTarget(0.1f) //
					.setAlignTolerance(0.001f) //
					.setDecelerationRadius(MathUtils.PI);
			BlendedSteering<Vector2> blendedSteering = new BlendedSteering<Vector2>(
					steerable) //
					.setLimiter(NullLimiter.NEUTRAL_LIMITER) //
					.add(arriveSB, 1f) //
					.add(lookWhereYouAreGoingSB, 1f);
			sb = blendedSteering;
		}

		entity.add(engine.createComponent(TargetFinderComponent.class));

		Components.FSM.get(entity).changeState(SquadComponent.DEFAULT_STATE);

		Components.STEERING_BEHAVIOR.get(entity).setBehavior(sb);

		entity.add(squadComp);
		entity.add(steerable);

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createProjectile(Vector2 position, Vector2 velocity,
			Faction faction, int damage) {
		Entity entity = buildEntity(position)
				.physicsBody(BodyType.DynamicBody)
				.circleSensor(Constants.projectileRadius)
				.filter(EntityCategory.PROJECTILE, 0,
						EntityCategory.UNIT | EntityCategory.RESOURCE)
				.faction(faction)
				.sprite(Assets.projectile.projectiles.get(faction.ordinal()),
						Constants.projectileRadius * 2,
						Constants.projectileRadius * 2).getWithoutAdding();

		ProjectileComponent projectileComp = engine.createComponent(
				ProjectileComponent.class).init(damage);
		entity.add(projectileComp);

		entity.add(engine.createComponent(DecayComponent.class).init(
				Constants.projectileDecayTime));

		PhysicsComponent physicsComp = Components.PHYSICS.get(entity);
		physicsComp.body.setBullet(true);
		physicsComp.body.setLinearVelocity(velocity);
		physicsComp.body.setTransform(position, velocity.angle());

		engine.addEntity(entity);
		return entity;
	}

	public static Entity createBoundry(Vector2 start, Vector2 end) {
		Entity entity = buildEntity(new Vector2(0, 0)).physicsBody(
				BodyType.StaticBody).getWithoutAdding();
		EdgeShape edge = new EdgeShape();
		edge.set(start, end);

		Components.PHYSICS.get(entity).body.createFixture(edge, 1.0f);
		engine.addEntity(entity);
		return entity;
	}

	public static Entity createBackgroundArt(Vector2 position, float width,
			float height, TextureRegion region, int layer) {
		Entity entity = buildEntity(position).sprite(region, width, height)
				.getWithoutAdding();

		entity.add(engine.createComponent(ParalaxComponent.class).init(
				position.x, position.y, width, height, layer));

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

		public EntityBuilder reset(Vector2 position) {
			this.position = position;
			entity = engine.createEntity();
			return this;
		}

		public EntityBuilder mothership() {
			entity.add(engine.createComponent(MothershipComponent.class));
			return this;
		}

		public EntityBuilder particle() {

			ParticleComponent p = engine.createComponent(
					ParticleComponent.class).init(Assets.particles.getEffect());
			p.setPosition(position.x, position.y);
			entity.add(p);

			return this;

		}

		public PhysicsBuilder buildPhysics(BodyType type) {
			return physicsBuilder.reset(type, position, entity);
		}

		public EntityBuilder physicsBody(BodyType type) {
			BodyDef def = new BodyDef();
			def.type = type;
			def.position.set(position);
			Body body = physicsSystem.createBody(def);
			body.setUserData(entity);

			PhysicsComponent physics = engine.createComponent(
					PhysicsComponent.class).init(body);
			entity.add(physics);
			return this;
		}

		public EntityBuilder damping(float angular, float linear) {
			if (Components.PHYSICS.has(entity)) {
				PhysicsComponent physics = Components.PHYSICS.get(entity);
				physics.body.setAngularDamping(angular);
				physics.body.setLinearDamping(linear);
			} else {
				Gdx.app.error(TAG, "entity is missing physics component!");
			}
			return this;
		}

		public EntityBuilder stateMachine() {
			FSMComponent stateMachineComp = engine.createComponent(
					FSMComponent.class).init(entity);
			entity.add(stateMachineComp);
			return this;
		}

		public EntityBuilder steeringBehavior() {
			SteeringBehaviorComponent behaviorComp = engine
					.createComponent(SteeringBehaviorComponent.class);
			entity.add(behaviorComp);
			return this;
		}

		public EntityBuilder resource(int amount) {
			ResourceComponent resourceComp = engine
					.createComponent(ResourceComponent.class);
			resourceComp.amount = amount;
			entity.add(resourceComp);

			return this;
		}

		public EntityBuilder filter(int categoryBits, int groupIndex,
				int maskBits) {
			entity.flags = categoryBits;

			Filter filter = new Filter();
			filter.categoryBits = (short) categoryBits;
			filter.groupIndex = (short) groupIndex;
			filter.maskBits = (short) maskBits;

			// TODO make EntityBuilder filter beter
			Components.PHYSICS.get(entity).body.getFixtureList().get(0)
					.setFilterData(filter);
			return this;
		}

		public EntityBuilder target() {
			entity.add(engine.createComponent(TargetComponent.class));
			return this;
		}

		public EntityBuilder weapon(int damage, float attackSpeed) {
			entity.add(engine.createComponent(WeaponComponent.class).init(
					damage, attackSpeed));
			return this;
		}

		public EntityBuilder faction(Faction faction) {
			FactionComponent factionComp = engine
					.createComponent(FactionComponent.class);
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

		public EntityBuilder circleCollider(float radius, float density) {
			CircleShape shape = new CircleShape();
			shape.setRadius(radius);
			PhysicsComponent physics = Components.PHYSICS.get(entity);
			if (physics == null) {
				physicsBody(DEFAULT_BODY);
			}

			physics.body.createFixture(shape, density);
			return this;
		}

		public EntityBuilder circleSensor(float radius) {
			CircleShape shape = new CircleShape();
			shape.setRadius(radius);

			PhysicsComponent physics = Components.PHYSICS.get(entity);
			if (physics == null) {
				physicsBody(DEFAULT_BODY);
			}

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.isSensor = true;
			fixtureDef.shape = shape;

			physics.body.createFixture(fixtureDef);
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

	public static class PhysicsBuilder {
		private Body body;

		public PhysicsBuilder reset(BodyType type, Vector2 position,
				Entity entity) {
			BodyDef def = new BodyDef();
			def.type = type;
			def.position.set(position);
			body = physicsSystem.createBody(def);
			body.setUserData(entity);
			return this;
		}

		public FixtureBuilder addFixture() {
			return fixtureBuilder.reset(body);
		}

		public EntityBuilder getBody() {
			return entityBuilder;
		}

		public static class FixtureBuilder {
			private Body body;
			private FixtureDef def = new FixtureDef();

			public FixtureBuilder reset(Body body) {
				this.body = body;
				def = new FixtureDef();
				return this;
			}

			public FixtureBuilder circle(float radius) {
				CircleShape circle = new CircleShape();
				circle.setRadius(radius);
				def.shape = circle;
				return this;
			}

			public PhysicsBuilder create() {
				body.createFixture(def);
				return physicsBuilder;
			}

		}

	}

}
