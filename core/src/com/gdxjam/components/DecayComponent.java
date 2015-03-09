
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class DecayComponent extends Component implements Poolable {

	public float elapsed = 0;
	public float decayTime;

	/** Can only be created by PooledEngine */
	private DecayComponent () {
		// private constructor
	}

	public DecayComponent init (float decayTime) {
		this.decayTime = decayTime;
		return this;
	}

	@Override
	public void reset () {
		elapsed = 0;
	}

}
