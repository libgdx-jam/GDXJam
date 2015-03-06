
package com.gdxjam.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Components {

	//Graphics
	public static final ComponentMapper<SpriteComponent> SPRITE = ComponentMapper.getFor(SpriteComponent.class);
	public static final ComponentMapper<ParticleComponent> PARTICLE = ComponentMapper.getFor(ParticleComponent.class);
	public static final ComponentMapper<ParalaxComponent> PARALAX = ComponentMapper.getFor(ParalaxComponent.class);

	//Combat
	public static final ComponentMapper<ProjectileComponent> PROJECTILE = ComponentMapper.getFor(ProjectileComponent.class);
	public static final ComponentMapper<DecayComponent> DECAY = ComponentMapper.getFor(DecayComponent.class);
	public static final ComponentMapper<TargetComponent> TARGET = ComponentMapper.getFor(TargetComponent.class);
	public static final ComponentMapper<TargetFinderComponent> TARGET_FINDER = ComponentMapper.getFor(TargetFinderComponent.class);
	public static final ComponentMapper<WeaponComponent> WEAPON = ComponentMapper.getFor(WeaponComponent.class);
	public static final ComponentMapper<HealthComponent> HEALTH = ComponentMapper.getFor(HealthComponent.class);

	public static final ComponentMapper<PhysicsComponent> PHYSICS = ComponentMapper.getFor(PhysicsComponent.class);
	public static final ComponentMapper<FactionComponent> FACTION = ComponentMapper.getFor(FactionComponent.class);
	public static final ComponentMapper<ResourceComponent> RESOURCE = ComponentMapper.getFor(ResourceComponent.class);

	public static final ComponentMapper<SquadMemberComponent> SQUAD_MEMBER = ComponentMapper.getFor(SquadMemberComponent.class);
	public static final ComponentMapper<SquadComponent> SQUAD = ComponentMapper.getFor(SquadComponent.class);

	// AI
	public static final ComponentMapper<BehaviorTreeComponent> BTREE = ComponentMapper.getFor(BehaviorTreeComponent.class);
	public static final ComponentMapper<SteeringBehaviorComponent> STEERING_BEHAVIOR = ComponentMapper
		.getFor(SteeringBehaviorComponent.class);
	public static final ComponentMapper<SteerableComponent> STEERABLE = ComponentMapper.getFor(SteerableComponent.class);

	public static final ComponentMapper<FSMComponent> FSM = ComponentMapper.getFor(FSMComponent.class);
}
