package com.gdxjam.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.utils.Location2;
import com.gdxjam.utils.Vector2Utils;

public class SteerableTarget implements Steerable<Vector2>{
	
	private Vector2 position;
	private float orientation;
	private float radius;
	
	public SteerableTarget(Vector2 position, float radius) {
		this.position = position;
		this.orientation = MathUtils.random(0, MathUtils.PI2);
		this.radius = radius;
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}
	
	public void setPosition(float x, float y){
		position.set(x, y);
	}
	
	@Override
	public float getBoundingRadius() {
		return radius;
	}
	

	//----------------------------------------------
	//=============================================
	//---------------------------------------------
	
	@Override
	public float getMaxLinearSpeed() {
		return 0;
	}
	
	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {}
	
	@Override
	public float getMaxLinearAcceleration() {
		return 0;
	}
	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {}

	@Override
	public float getMaxAngularSpeed() {
		return 0;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {}

	@Override
	public float getMaxAngularAcceleration() {
		return 0;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {}


	@Override
	public float getOrientation() {
		return orientation;
	}

	@Override
	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return Vector2.Zero;
	}

	@Override
	public float getAngularVelocity() {
		return 0;
	}

	@Override
	public boolean isTagged() {
		return false;
	}

	@Override
	public void setTagged(boolean tagged) {}

	@Override
	public Location<Vector2> newLocation() {
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
