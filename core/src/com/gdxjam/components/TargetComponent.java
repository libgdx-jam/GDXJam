
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TargetComponent extends Component implements Poolable {

	private Entity target;

	/** Can only be created by PooledEngine */
	private TargetComponent () {
		// private constructor
	}

	public TargetComponent init (Entity target) {
		this.target = target;
		return this;
	}

	public void setTarget (Entity entity) {
		this.target = entity;
	}

	public Entity getTarget () {
		return target;
	}

	@Override
	public void reset () {
		target = null;
	}

}
