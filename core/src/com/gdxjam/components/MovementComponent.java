package com.gdxjam.components;

import com.badlogic.ashley.core.Component;

public class MovementComponent extends Component {
	public SteerableEntity entity;

	public MovementComponent() {
		entity = new SteerableEntity(false);
	}
}