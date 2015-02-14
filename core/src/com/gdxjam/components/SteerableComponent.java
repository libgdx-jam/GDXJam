package com.gdxjam.components;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SteerableComponent implements Steerable<Vector2> {

	private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(
			new Vector2());

	TextureRegion region;

	Vector2 position; // like scene2d centerX and centerY, but we need a vector
						// to implement Steerable
	Vector2 linearVelocity;
	float angularVelocity;
	float boundingRadius;
	boolean tagged;

	float maxLinearSpeed = 100;
	float maxLinearAcceleration = 200;
	float maxAngularSpeed = 5;
	float maxAngularAcceleration = 10;

	boolean independentFacing;

	SteeringBehavior<Vector2> steeringBehavior;

	public SteerableComponent init(TextureRegion region, boolean independentFacing) {
		this.independentFacing = independentFacing;
		this.region = region;
		this.independentFacing = independentFacing;
		this.region = region;
		this.position = new Vector2();
		this.linearVelocity = new Vector2();
		// this.setBounds(0, 0, region.getRegionWidth(),
		// region.getRegionHeight());
		this.boundingRadius = (region.getRegionWidth() + region
				.getRegionHeight()) / 4f;
		// this.setOrigin(region.getRegionWidth() * .5f,
		// region.getRegionHeight() * .5f);
		return this;
	}

	// public void setBounds(float x, float y, float width, float height){
	// if (this.x != x || this.y != y) {
	// this.x = x;
	// this.y = y;
	// positionChanged();
	// }
	// if (this.width != width || this.height != height) {
	// this.width = width;
	// this.height = height;
	// sizeChanged();
	// }
	// }

	@Override
	public float getMaxLinearSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getMaxLinearAcceleration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getMaxAngularSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getMaxAngularAcceleration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector2 getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getOrientation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2 getLinearVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getAngularVelocity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getBoundingRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isTagged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTagged(boolean tagged) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector2 newVector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPosition(float x, float y) {
		position = new Vector2(x, y);
	}

}
