package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.gdxjam.GameManager;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.screens.GameOverScreen;

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

			if (Components.MOTHERSHIP.has(entity)) {
				GameManager.setScreen(new GameOverScreen());
			}

			engine.removeEntity(entity);
			return;
		}

		health.value = MathUtils.clamp(health.value, health.min, health.max);

	}
	
	@Override
	public boolean checkProcessing () {
		return !GameManager.isPaused();
	}

}
