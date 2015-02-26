package com.gdxjam.utils;

import com.badlogic.gdx.math.Vector2;

public final class Vector2Utils {

	private Vector2Utils() {
	}

	public static float vectorToAngle(Vector2 vector) {
		return vector.angleRad();
	}

	public static Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float) Math.sin(angle);
		outVector.y = (float) Math.cos(angle);
		return outVector;
	}

}
