package com.gdxjam.ai.test;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SteerableGuy implements Steerable<Vector2> {

	public static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(
			new Vector2());

	SteeringBehavior<Vector2> steeringBehavior;

	public float maxLinearSpeed;
	public float maxLinearAcceleration;
	public float maxAngularSpeed;
	public float maxAngularAcceleration;
	public Vector2 position;
	public Vector2 linearVelocity;
	public float angularVelocity;
	public float rotation;
	public boolean tagged;
	public float boundingRadius;
	boolean independentFacing;

	public SteerableGuy(boolean independentFacing) {
		this.independentFacing = independentFacing;
		this.position = new Vector2();
		this.linearVelocity = new Vector2();
	}

	@Override
	public float getMaxLinearSpeed() {
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		// TODO Auto-generated method stub
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public float getOrientation() {
		return getRotation() * MathUtils.degreesToRadians;
	}

	public float getRotation() {
		return rotation;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return linearVelocity;
	}

	@Override
	public float getAngularVelocity() {
		return angularVelocity;
	}

	@Override
	public float getBoundingRadius() {
		return boundingRadius;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}

	@Override
	public Vector2 newVector() {
		return new Vector2();
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return (float) Math.atan2(-vector.x, vector.y);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float) Math.sin(angle);
		outVector.y = (float) Math.cos(angle);
		return outVector;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void update(float delta) {
		if (steeringBehavior != null) {

			// Calculate steering acceleration
			steeringBehavior.calculateSteering(steeringOutput);

			/*
			 * Here you might want to add a motor control layer filtering
			 * steering accelerations.
			 * 
			 * For instance, a car in a driving game has physical constraints on
			 * its movement: it cannot turn while stationary; the faster it
			 * moves, the slower it can turn (without going into a skid); it can
			 * brake much more quickly than it can accelerate; and it only moves
			 * in the direction it is facing (ignoring power slides).
			 */

			// Apply steering acceleration
			applySteering(steeringOutput, delta);

			setPosition(position.x, position.y);
		}
	}

	public void setPosition(float x, float y) {
		position.set(x, y);
	}

	public boolean isIndependentFacing() {
		return independentFacing;
	}

	public void setIndependentFacing(boolean independentFacing) {
		this.independentFacing = independentFacing;
	}

	public SteeringBehavior<Vector2> getSteeringBehavior() {
		return steeringBehavior;
	}

	public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
		this.steeringBehavior = steeringBehavior;
	}

	public void applySteering(SteeringAcceleration<Vector2> steering, float time) {
		// Update position and linear velocity. Velocity is trimmed to maximum
		// speed
		position.mulAdd(linearVelocity, time);
		linearVelocity.mulAdd(steering.linear, time).limit(getMaxLinearSpeed());

		// Update orientation and angular velocity
		if (independentFacing) {
			setRotation(getRotation() + (angularVelocity * time)
					* MathUtils.radiansToDegrees);
			angularVelocity += steering.angular * time;
		} else {
			// If we haven't got any velocity, then we can do nothing.
			if (!linearVelocity.isZero(MathUtils.FLOAT_ROUNDING_ERROR)) {
				float newOrientation = vectorToAngle(linearVelocity);
				angularVelocity = (newOrientation - getRotation()
						* MathUtils.degreesToRadians)
						* time; // this is superfluous if independentFacing is
								// always true
				setRotation(newOrientation * MathUtils.radiansToDegrees);
			}
		}
	}

}
