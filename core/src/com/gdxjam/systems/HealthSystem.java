package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.gdxjam.Assets;
import com.gdxjam.AudioManager;
import com.gdxjam.GameManager;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.ecs.EntityCategory;
import com.gdxjam.screens.GameOverScreen;
import com.gdxjam.systems.ParticleSystem.ParticleType;
import com.gdxjam.utils.EntityFactory;

public class HealthSystem extends IteratingSystem {

	private PooledEngine engine;

	public HealthSystem() {
		super(Family.all(HealthComponent.class).get());
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.engine = (PooledEngine) engine;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		HealthComponent health = Components.HEALTH.get(entity);

		if (health.value <= health.min) {

			if((entity.flags & EntityCategory.MOTHERSHIP) > 0){
				GameManager.setScreen(new GameOverScreen());
			}
			engine.removeEntity(entity);
			AudioManager.playSound(Assets.sound.boom);
			EntityFactory.createParticle(Components.STEERABLE.get(entity).getPosition(), ParticleType.EXPLOSION);
			return;
		}

		health.value = MathUtils.clamp(health.value, health.min, health.max);

	}
	
	@Override
	public boolean checkProcessing () {
		return !GameManager.isPaused();
	}

}
