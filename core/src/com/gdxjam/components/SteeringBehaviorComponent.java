
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class SteeringBehaviorComponent extends Component implements Poolable {

	private SteeringBehavior<Vector2> behavior;

	/** Can only be created by PooledEngine */
	private SteeringBehaviorComponent () {
		// private constructor
	}

	public SteeringBehaviorComponent init (SteeringBehavior<Vector2> behavior) {
		setBehavior(behavior);
		return this;
	}

	public void setBehavior (SteeringBehavior<Vector2> behavior) {
		this.behavior = behavior;
	}

	public SteeringBehavior<Vector2> getBehavior () {
		return behavior;
	}

	@Override
	public void reset () {
		behavior = null;
	}

}
