package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent extends Component {
	public Vector2 velocity;

	public VelocityComponent(float velocityX, float velocityY) {
		this(new Vector2(velocityX,velocityY));
	}

    public VelocityComponent(Vector2 velocity) {
        this.velocity = velocity;
    }
}