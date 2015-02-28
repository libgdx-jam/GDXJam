
package com.gdxjam;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.gdxjam.components.Components;
import com.gdxjam.components.RemovalComponent;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.EntityRenderSystem;
import com.gdxjam.systems.HUDSystem;
import com.gdxjam.systems.HealthSystem;
import com.gdxjam.systems.InputSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.ResourceSystem;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.systems.StateMachineSystem;
import com.gdxjam.systems.SteeringSystem;
import com.gdxjam.utils.Constants;

public class EntityManager extends PooledEngine implements Disposable {
	private static String TAG = "[" + EntityManager.class.getSimpleName() + "]";

	public EntityManager () {
		initSystems();
	}

	private EntityManager initSystems () {
		CameraSystem cameraSystem = new CameraSystem(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		addSystem(cameraSystem);

		addSystem(new PhysicsSystem());

		// AI
		addSystem(new SteeringSystem());
		addSystem(new StateMachineSystem());

		addSystem(new HealthSystem());

		addSystem(new HUDSystem(Assets.getManager().get(Assets.SKIN, Skin.class)));

		addSystem(new ResourceSystem());
		addSystem(new SquadSystem());

		InputSystem input = new InputSystem();
		addSystem(input);
		Gdx.input.setInputProcessor(input.getMultiplexer());

		// Renderering happens last
		addSystem(new EntityRenderSystem());
		return this;
	}

	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);

		for (Entity entity : getEntitiesFor(Family.all(RemovalComponent.class).get())) {
			removeEntity(entity);
		}
	}

	@Override
	protected void removeEntityInternal (Entity entity) {
		if (Components.PHYSICS.has(entity)) {
			Body body = Components.PHYSICS.get(entity).body;
			getSystem(PhysicsSystem.class).destroyBody(body);
		}

		super.removeEntityInternal(entity);
	}

	@Override
	public void dispose () {
		Gdx.app.log(TAG, "disposing instance");
		removeAllEntities();
		clearPools();
		for (EntitySystem system : getSystems()) {
			if (system instanceof Disposable) {
				((Disposable)system).dispose();
			}
			system = null;
			removeSystem(system);
		}
	}

}
