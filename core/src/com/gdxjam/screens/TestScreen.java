package com.gdxjam.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.EntityManager;
import com.gdxjam.input.DefaultInputProcessor;
import com.gdxjam.input.DesktopGestureListener;
import com.gdxjam.input.InputAITest;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.CommanderControllerSystem;
import com.gdxjam.systems.EntityRenderSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.SteeringSystem;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.GUItest;
import com.gdxjam.utils.generators.WorldGenerator;

public class TestScreen extends AbstractScreen {
	
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
		inputMultiplexer.addProcessor(new DefaultInputProcessor());
		inputMultiplexer.addProcessor(new InputAITest(engine));
		inputMultiplexer.addProcessor(new GestureDetector(new DesktopGestureListener(engine)));
		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	public void createTestWorld() {
		EntityFactory.createSquad(new Vector2(15, 5));
		EntityFactory.createSquad(new Vector2(-30, -30));
		EntityFactory.createSquad(new Vector2(0, 30));
		EntityFactory.createSquad(new Vector2(50, 5));
		WorldGenerator.generateForest(new Vector2(20, 20), 25, 7, 0.1f, 0.2f, 0.1f, "tree");
		WorldGenerator.generateForest(new Vector2(-10, 5), 10, 30, 0.1f, 0.2f, 0.05f, "tree");
		
		WorldGenerator.generateForest(new Vector2(20, -20), 50, 15, 0.5f, 2.0f, 0.50f, "mushroom");
		EntityFactory.createFortress(new Vector2(15, 5), 15, 12);
	}

	public void initEngine() {
		engine = EntityManager.getInstance().initSystems();
		engine.addSystem(new CommanderControllerSystem());
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		engine.update(delta);
		GUITest.act(delta);
		GUITest.draw();
	}

}
