package com.gdxjam.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Components {
	
	public static final ComponentMapper<SpriteComponent> SPRITE = ComponentMapper.getFor(SpriteComponent.class);
	
	public static final ComponentMapper<SteeringBehaviorComponent> STEERING_BEHAVIOR = ComponentMapper.getFor(SteeringBehaviorComponent.class);
	public static final ComponentMapper<SteerableBodyComponent> STEERABLE_BODY = ComponentMapper.getFor(SteerableBodyComponent.class);

    public static final ComponentMapper<StateMachineComponent> STATE_MACHINE = ComponentMapper.getFor(StateMachineComponent.class);
    public static final ComponentMapper<ProximityComponent> PROXIMITY = ComponentMapper.getFor(ProximityComponent.class);
    public static final ComponentMapper<CommanderComponent> COMMANDER = ComponentMapper.getFor(CommanderComponent.class);
}
