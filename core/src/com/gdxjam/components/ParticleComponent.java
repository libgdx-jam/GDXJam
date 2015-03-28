
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ParticleComponent extends Component implements Poolable{
	public PooledEffect effect;

	/** Can only be created by PooledEngine */
	private ParticleComponent () {
		// private constructor
	}
	public ParticleComponent init (PooledEffect effect) {
		this.effect = effect;
		return this;
	}
	
	@Override
	public void reset () {
		effect.free();
	}

}
