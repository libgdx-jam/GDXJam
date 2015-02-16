package com.gdxjam.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector2;

public class SteerableTarget implements Steerable<Vector2>{
	
	private Vector2 target;
	private float radius;
	
	public SteerableTarget(Vector2 target, float radius) {
		this.target = target;
		this.radius = radius;
	}

	@Override
	public Vector2 getPosition() {
		return target;
	}
	
	public void setPosition(float x, float y){
		target.set(x, y);
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
		return 0;
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
	public Vector2 newVector() {
		return new Vector2();
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}

}
