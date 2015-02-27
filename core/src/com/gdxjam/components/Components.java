package com.gdxjam.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Components {
	
	public static final ComponentMapper<SpriteComponent> SPRITE = ComponentMapper.getFor(SpriteComponent.class);
	public static final ComponentMapper<NinePatchComponent> NINE_PATCH = ComponentMapper.getFor(NinePatchComponent.class);
	
	public static final ComponentMapper<HealthComponent> HEALTH = ComponentMapper.getFor(HealthComponent.class);
	public static final ComponentMapper<PhysicsComponent> PHYSICS = ComponentMapper.getFor(PhysicsComponent.class);
	public static final ComponentMapper<FactionComponent> FACTION = ComponentMapper.getFor(FactionComponent.class);
	public static final ComponentMapper<ResourceComponent> RESOURCE = ComponentMapper.getFor(ResourceComponent.class);
	
	//AI
	public static final ComponentMapper<CommanderComponent> COMMANDER = ComponentMapper.getFor(CommanderComponent.class);
	public static final ComponentMapper<UnitComponent> UNIT = ComponentMapper.getFor(UnitComponent.class);
	public static final ComponentMapper<SteeringBehaviorComponent> STEERING_BEHAVIOR = ComponentMapper.getFor(SteeringBehaviorComponent.class);
	public static final ComponentMapper<SteerableBodyComponent> STEERABLE_BODY = ComponentMapper.getFor(SteerableBodyComponent.class);

    public static final ComponentMapper<StateMachineComponent> STATE_MACHINE = ComponentMapper.getFor(StateMachineComponent.class);
    public static final ComponentMapper<ProximityComponent> PROXIMITY = ComponentMapper.getFor(ProximityComponent.class);
}
