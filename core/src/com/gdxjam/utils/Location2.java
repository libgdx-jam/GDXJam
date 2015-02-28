package com.gdxjam.utils;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

/**
 * {@code Location2} represents an object having a 2D position and an
 * orientation.
 * 
 * @author davebaol
 */
public class Location2 implements Location<Vector2> {

	private Vector2 position;
	private float orientation;

	public Location2() {
		this(new Vector2(), 0);
	}

	public Location2(Vector2 position) {
		this(position, 0);
	}

	public Location2(Vector2 position, float orientation) {
		this.position = position;
		this.orientation = orientation;
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public float getOrientation() {
		return orientation;
	}

	@Override
	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}

	@Override
	public Location2 newLocation() {
		return new Location2();
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return Vector2Utils.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		return Vector2Utils.angleToVector(outVector, angle);
	}
}
