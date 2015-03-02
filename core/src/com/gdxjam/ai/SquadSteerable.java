package com.gdxjam.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.utils.Location2;

public class SquadSteerable implements Steerable<Vector2>{
	
	private static final float MAX_LINEAR_SPEED = 8f;
	private static final float MAX_LINEAR_ACCELERATION = 100000;
	private static final float MAX_ANGULAR_SPEED = 30;
	private static final float MAX_ANGULAR_AACCELERATION = 100;

	private float maxLinearSpeed = MAX_LINEAR_SPEED;
	private float maxLinearAcceleration = MAX_LINEAR_ACCELERATION;
	private float maxAngluarSpeed = MAX_ANGULAR_SPEED;
	private float maxAngluarAcceleration = MAX_ANGULAR_AACCELERATION;
	private boolean independentFacing = false;
	
	private Vector2 maxLinearVelocity;
	private Vector2 linearVelocity;
	private float angularVelocity;
	
	private Location2 location;

	@Override
	public Vector2 getPosition () {
		return location.getPosition();
	}

	@Override
	public float getOrientation () {
		return location.getOrientation();
	}

	@Override
	public void setOrientation (float orientation) {
		location.setOrientation(orientation);
	}

	@Override
	public float vectorToAngle (Vector2 vector) {
		return location.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector (Vector2 outVector, float angle) {
		return location.angleToVector(outVector, angle);
	}

	@Override
	public Location<Vector2> newLocation () {
		return new Location2();
	}

	@Override
	public float getMaxLinearSpeed () {
		return maxAngluarSpeed;
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
	public Vector2 getLinearVelocity () {
		return maxLinearVelocity;
	}

	@Override
	public float getAngularVelocity () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getBoundingRadius () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isTagged () {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTagged (boolean tagged) {
		// TODO Auto-generated method stub
		
	}

}
