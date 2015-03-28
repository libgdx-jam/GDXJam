package com.gdxjam.ecs;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.gdxjam.components.PhysicsComponent;
import com.gdxjam.components.ResourceComponent;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.UnitComponent;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.ConstructionSystem;
import com.gdxjam.systems.DecaySystem;
import com.gdxjam.systems.EntityRenderSystem;
import com.gdxjam.systems.FSMSystem;
import com.gdxjam.systems.GUISystem;
import com.gdxjam.systems.HealthSystem;
import com.gdxjam.systems.InputSystem;
import com.gdxjam.systems.ParticleSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.ResourceSystem;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.systems.SteeringSystem;
import com.gdxjam.systems.WaveSystem;
import com.gdxjam.utils.Constants;

public class EntityManager extends PooledEngine implements Disposable {
	private static String TAG = "[" + EntityManager.class.getSimpleName() + "]";

	public EntityManager() {
		initSystems();

		addEntityListener(Family.all(SquadComponent.class).get(),
				new SquadEntityListener(this, getSystem(InputSystem.class)));
		addEntityListener(Family.all(UnitComponent.class).get(),
				new UnitEntityListener(this));
		addEntityListener(Family.all(PhysicsComponent.class).get(),
				new PhysicsEntityListener(getSystem(PhysicsSystem.class)));
		
		addEntityListener(Family.all(ResourceComponent.class).get(), new ResourceEntityListener(this));
		addEntityListener(new DebugEntityListener());
	}

	private EntityManager initSystems() {
		CameraSystem cameraSystem = new CameraSystem(Constants.VIEWPORT_WIDTH,
				Constants.VIEWPORT_HEIGHT);
		addSystem(cameraSystem);

		addSystem(new PhysicsSystem());

		// AI
		addSystem(new SteeringSystem());
		addSystem(new FSMSystem());

		addSystem(new HealthSystem());


		GUISystem guiSystem = new GUISystem();
		InputSystem inputSystem = new InputSystem(guiSystem);

		ResourceSystem resourceSystem = new ResourceSystem(guiSystem);
		addSystem(resourceSystem);
		
		ConstructionSystem constructSystem = new ConstructionSystem(resourceSystem);
		addSystem(new SquadSystem(inputSystem));
		addSystem(new WaveSystem(guiSystem));
		addSystem(new DecaySystem());

		addSystem(inputSystem);
		// Rendering happens last
		addSystem(new EntityRenderSystem());
		addSystem(new ParticleSystem());
		addSystem(guiSystem);

		return this;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
//		getSystem(PhysicsSystem.class).drawDebug();
	}

	@Override
	public void dispose() {
		Gdx.app.log(TAG, "disposing instance");
		removeAllEntities();
		clearPools();
		for (EntitySystem system : getSystems()) {
			if (system instanceof Disposable) {
				((Disposable) system).dispose();
			}
			system = null;
			removeSystem(system);
		}
	}

}
