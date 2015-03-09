
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class HealthComponent extends Component implements Poolable {

	public int min = 0;
	public int max = 100;
	public int value = 100;

	/** Can only be created by PooledEngine */
	private HealthComponent () {
		// private constructor
	}

	@Override
	public void reset () {
		value = 100;
	}

}
