
package com.gdxjam.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.gdxjam.components.BehaviorTreeComponent;
import com.gdxjam.components.DecayComponent;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.components.FactionComponent;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.ParalaxComponent;
import com.gdxjam.components.ParticleComponent;
import com.gdxjam.components.PhysicsComponent;
import com.gdxjam.components.ProjectileComponent;
import com.gdxjam.components.ResourceComponent;
import com.gdxjam.components.SpriteComponent;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.components.UnitComponent;
import com.gdxjam.components.WeaponComponent;

public class Components {

	// Entity types
	public static final ComponentMapper<SquadComponent> SQUAD = ComponentMapper.getFor(SquadComponent.class);
	public static final ComponentMapper<UnitComponent> UNIT = ComponentMapper.getFor(UnitComponent.class);
	public static final ComponentMapper<ResourceComponent> RESOURCE = ComponentMapper.getFor(ResourceComponent.class);
	public static final ComponentMapper<ProjectileComponent> PROJECTILE = ComponentMapper.getFor(ProjectileComponent.class);

	public static final ComponentMapper<PhysicsComponent> PHYSICS = ComponentMapper.getFor(PhysicsComponent.class);
	public static final ComponentMapper<FactionComponent> FACTION = ComponentMapper.getFor(FactionComponent.class);

	// AI
	public static final ComponentMapper<BehaviorTreeComponent> BTREE = ComponentMapper.getFor(BehaviorTreeComponent.class);
	public static final ComponentMapper<SteeringBehaviorComponent> STEERING_BEHAVIOR = ComponentMapper
		.getFor(SteeringBehaviorComponent.class);
	public static final ComponentMapper<SteerableComponent> STEERABLE = ComponentMapper.getFor(SteerableComponent.class);
	public static final ComponentMapper<FSMComponent> FSM = ComponentMapper.getFor(FSMComponent.class);

	// Graphics
	public static final ComponentMapper<SpriteComponent> SPRITE = ComponentMapper.getFor(SpriteComponent.class);
	public static final ComponentMapper<ParticleComponent> PARTICLE = ComponentMapper.getFor(ParticleComponent.class);
	public static final ComponentMapper<ParalaxComponent> PARALAX = ComponentMapper.getFor(ParalaxComponent.class);

	// Combat

	public static final ComponentMapper<DecayComponent> DECAY = ComponentMapper.getFor(DecayComponent.class);
	public static final ComponentMapper<TargetComponent> TARGET = ComponentMapper.getFor(TargetComponent.class);
	public static final ComponentMapper<WeaponComponent> WEAPON = ComponentMapper.getFor(WeaponComponent.class);
	public static final ComponentMapper<HealthComponent> HEALTH = ComponentMapper.getFor(HealthComponent.class);

}
