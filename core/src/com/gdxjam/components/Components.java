package com.gdxjam.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Components {

	public static final ComponentMapper<SpriteComponent> SPRITE = ComponentMapper
			.getFor(SpriteComponent.class);
	public static final ComponentMapper<ParalaxComponent> PARALAX = ComponentMapper
		.getFor(ParalaxComponent.class);
	
	public static final ComponentMapper<HealthComponent> HEALTH = ComponentMapper
			.getFor(HealthComponent.class);
	public static final ComponentMapper<PhysicsComponent> PHYSICS = ComponentMapper
			.getFor(PhysicsComponent.class);
	public static final ComponentMapper<FactionComponent> FACTION = ComponentMapper
			.getFor(FactionComponent.class);
	public static final ComponentMapper<ResourceComponent> RESOURCE = ComponentMapper
			.getFor(ResourceComponent.class);

	public static final ComponentMapper<SquadMemberComponent> SQUAD_MEMBER = ComponentMapper
			.getFor(SquadMemberComponent.class);

	// AI
	public static final ComponentMapper<SteeringBehaviorComponent> STEERING_BEHAVIOR = ComponentMapper
			.getFor(SteeringBehaviorComponent.class);
	public static final ComponentMapper<SteerableComponent> STEERABLE = ComponentMapper
			.getFor(SteerableComponent.class);

	public static final ComponentMapper<StateMachineComponent> STATE_MACHINE = ComponentMapper
			.getFor(StateMachineComponent.class);
}
