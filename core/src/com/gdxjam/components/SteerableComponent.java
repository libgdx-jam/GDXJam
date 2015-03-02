
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.Location2;
import com.gdxjam.utils.Vector2Utils;

public class SteerableComponent extends Component implements Steerable<Vector2>, Poolable {

	public static final float MAX_LINEAR_SPEED = 12f;
	public static final float MAX_LINEAR_ACCELERATION = 100000;
	public static final float MAX_ANGULAR_SPEED = 5;
	public static final float MAX_ANGULAR_ACCELERATION = 100;

	private float maxLinearSpeed = MAX_LINEAR_SPEED;
	private float maxLinearAcceleration = MAX_LINEAR_ACCELERATION;
	private float maxAngluarSpeed = MAX_ANGULAR_SPEED;
	private float maxAngluarAcceleration = MAX_ANGULAR_ACCELERATION;
	private boolean independentFacing = true;

	private boolean tagged = false;

	private Body body;

	public Body getBody () {
		return body;
	}

	public SteerableComponent init (Body body) {
		this.body = body;
		return this;
	}

	@Override
	public float getMaxLinearSpeed () {
		return maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed (float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration () {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration (float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed () {
		return maxAngluarSpeed;
	}

	@Override
	public void setMaxAngularSpeed (float maxAngularSpeed) {
		this.maxAngluarSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration () {
		return maxAngluarAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration (float maxAngularAcceleration) {
		this.maxAngluarAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getPosition () {
		return body.getPosition();
	}

	@Override
	public float getOrientation () {
		return body.getAngle();
	}

	@Override
	public void setOrientation (float orientation) {
		body.setTransform(getPosition(), orientation);
	}

	@Override
	public Vector2 getLinearVelocity () {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity () {
		return body.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius () {
		return Constants.unitRadius;
	}

	@Override
	public boolean isTagged () {
		return tagged;
	}

	@Override
	public void setTagged (boolean tagged) {
		this.tagged = tagged;
	}

	public void setIndependentFacing (boolean independentFacing) {
		this.independentFacing = independentFacing;
	}

	public boolean isIndependentFacing () {
		return independentFacing;
	}

	@Override
	public Location<Vector2> newLocation () {
		return new Location2();
	}

	@Override
	public float vectorToAngle (Vector2 vector) {
		return Vector2Utils.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector (Vector2 outVector, float angle) {
		return Vector2Utils.angleToVector(outVector, angle);
	}

	@Override
	public void reset () {
		maxLinearSpeed = MAX_LINEAR_SPEED;
		maxLinearAcceleration = MAX_LINEAR_ACCELERATION;

		maxAngluarSpeed = MAX_ANGULAR_SPEED;
		maxAngluarAcceleration = MAX_ANGULAR_ACCELERATION;
		independentFacing = false;

		tagged = false;
	}

}
