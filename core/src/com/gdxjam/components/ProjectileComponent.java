
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ProjectileComponent extends Component implements Poolable {

	private int damage;

	/** Can only be created by PooledEngine */
	private ProjectileComponent () {
		// private constructor
	}

	public ProjectileComponent init (int damage) {
		this.damage = damage;
		return this;
	}

	public int getDamage () {
		return damage;
	}

	@Override
	public void reset () {

	}

}
