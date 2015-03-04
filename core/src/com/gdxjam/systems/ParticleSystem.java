package com.gdxjam.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdxjam.Assets;
import com.gdxjam.components.Components;
import com.gdxjam.components.ParticleComponent;

public class ParticleSystem extends IteratingSystem {

	SpriteBatch batch;
	OrthographicCamera camera;

	public ParticleSystem() {
		super(Family.all(ParticleComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		batch = new SpriteBatch();

	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ParticleComponent particle = Components.PARTICLE.get(entity);
		particle.update(deltaTime);

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		batch.begin();
		for (Entity e : getEntities()) {
			ParticleComponent particle = Components.PARTICLE.get(e);
			particle.draw(batch);
			if (particle.effect.isComplete()) {
				particle.effect.free();
				Assets.particles.effects.removeValue(particle.effect, true);
				if (Components.PARTICLE.has(e)) {
					e.remove(ParticleComponent.class);

				}
			}

		}
		batch.end();
	}

}
