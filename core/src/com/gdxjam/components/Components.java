package com.gdxjam.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Components {
	
	public static final ComponentMapper<PositionComponent> POSITION = ComponentMapper.getFor(PositionComponent.class);
	public static final ComponentMapper<VisualComponent> VISUAL = ComponentMapper.getFor(VisualComponent.class);

}
