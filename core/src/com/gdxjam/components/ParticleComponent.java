package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticleComponent extends Component {
	public PooledEffect effect;

	public void update(float delta) {
		effect.update(delta);
	}

	public void draw(SpriteBatch batch) {
		effect.draw(batch);
	}

	public ParticleComponent init(PooledEffect effect) {
		this.effect = effect;
		return this;
	}

	public void setPosition(float x, float y) {
		effect.setPosition(x, y);
		System.out.println("Particle @x: " + x + " y: " + y);
	}

}
