package com.gdxjam.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.systems.*;
import com.gdxjam.InputAITest;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.GUItest;
import com.gdxjam.utils.generators.ResourceGenerator;

public class TestScreen2 extends AbstractScreen {

	private static final int PIXELS_PER_UNIT = 32;

	private PooledEngine engine;
	private GUItest GUITest;

	@Override
	public void show() {
		InputMultiplexer inputMultiplexer = new InputMultiplexer();

		initEngine();
		// gui needs to be created after engine but before world, for the
		// listener to work
		GUITest = new GUItest(engine);

		createTestWorld();

		// inputs
		inputMultiplexer.addProcessor(GUITest);
		inputMultiplexer.addProcessor(new InputAITest(engine));
		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	public void createTestWorld() {
		EntityFactory.createSquad(new Vector2(15, 5));
		EntityFactory.createSquad(new Vector2(-30, -30));
		EntityFactory.createSquad(new Vector2(0, 30));
		EntityFactory.createSquad(new Vector2(50, 5));
		ResourceGenerator.generateForest(engine, new Vector2(20, 20), 25, 7, 0,
				0.1f);
		EntityFactory.createFortress(new Vector2(15, 5), 15, 12);
		ResourceGenerator.generateForest(engine, new Vector2(-10, 5), 10, 30,
				0, 0.05f);
	}

	public void initEngine() {
		engine = new PooledEngine();
		EntityFactory.setEngine(engine);

		OrthographicCamera camera = new OrthographicCamera(
				Gdx.graphics.getWidth() / PIXELS_PER_UNIT,
				Gdx.graphics.getHeight() / PIXELS_PER_UNIT);
		camera.position.set(10, 10, 0);
		CameraSystem cameraSystem = new CameraSystem(camera);
		engine.addSystem(cameraSystem);

		PhysicsSystem physicsSystem = new PhysicsSystem();
		engine.addSystem(physicsSystem);

		SteeringSystem steeringSystem = new SteeringSystem();
		engine.addSystem(steeringSystem);

		engine.addSystem(new CommanderControllerSystem());

		engine.addSystem(new EntityRenderSystem(cameraSystem.getCamera()));
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		engine.update(delta);
		GUITest.act(delta);
		GUITest.draw();
	}

}
