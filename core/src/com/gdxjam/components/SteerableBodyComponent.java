package com.gdxjam.components;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class SteerableBodyComponent extends PhysicsComponent implements Steerable<Vector2>, Poolable{
	
	private float maxLinearSpeed = 5f;
	private float maxLinearAcceleration = 1000;
	
	private float maxAngluarSpeed = 30;
	private float maxAngluarAcceleration = 100;
	private boolean independentFacing = false;
	
	private boolean tagged = false;

	@Override
	public float getMaxLinearSpeed() {
		return maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngluarSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngluarSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngluarAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngluarAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public float getOrientation() {
		return body.getAngle();
	}

	@Override
	public Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return 0.5f;	//TODO bounding radius
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}
	
	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}
	
	public void setIndependentFacing(boolean independentFacing){
		this.independentFacing = independentFacing;
	}
	
	public boolean isIndependentFacing(){
		return independentFacing;
	}

	@Override
	public Vector2 newVector() {
		return new Vector2();
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return vector.angleRad();
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}
	
	

}
