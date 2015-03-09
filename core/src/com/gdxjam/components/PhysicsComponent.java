
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PhysicsComponent extends Component implements Poolable {

// public enum SteerableParameter{
//
// UNIT(5.0f, 300.0f, 30.0f, 100.0f, false),
// STATIC(0.0f, 0.0f, 0.0f, 0.0f, false);
//
// public float maxLinearSpeed;
// public float maxLinearAcceleration;
//
// public float maxAngluarSpeed = 30;
// public float maxAngluarAcceleration = 100;
//
// public boolean independentFacing = false;
//
// private SteerableParameter(
// float maxLinearSpeed,
// float maxLinearAcceleration,
// float maxAngluarSpeed,
// float maxAngluarAcceleration, boolean independentFacing){
//
// this.maxLinearSpeed = maxLinearSpeed;
// this.maxLinearAcceleration = maxLinearAcceleration;
//
// this.maxAngluarSpeed = maxAngluarSpeed;
// this.maxLinearAcceleration = maxAngluarAcceleration;
// this.independentFacing = independentFacing;
// }
// }

	private Body body;

	/** Can only be created by PooledEngine */
	private PhysicsComponent () {
		// private constructor
	}

	public PhysicsComponent init (Body body) {
		this.body = body;
		return this;
	}

	public Body getBody () {
		return body;
	}

	@Override
	public void reset () {
		body = null;
	}

}
