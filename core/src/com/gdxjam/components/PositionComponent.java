package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent extends Component {
	public Vector2 position;

	public PositionComponent(float x, float y) {
		position = new Vector2(x, y);

	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}
}
