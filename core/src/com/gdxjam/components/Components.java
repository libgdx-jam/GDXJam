package com.gdxjam.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Components {
	
	public static final ComponentMapper<SpriteComponent> SPRITE = ComponentMapper.getFor(SpriteComponent.class);
	public static final ComponentMapper<VisualComponent> VISUAL = ComponentMapper.getFor(VisualComponent.class);
	
	public static final ComponentMapper<SteeringBehaviorComponent> STEERING_BEHAVIOR = ComponentMapper.getFor(SteeringBehaviorComponent.class);
	public static final ComponentMapper<SteerableBodyComponent> STEERABLE_BODY = ComponentMapper.getFor(SteerableBodyComponent.class);

}
