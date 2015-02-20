package com.gdxjam.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.GameWorld;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.EntityRenderSystem;
import com.gdxjam.systems.GUISystem;
import com.gdxjam.systems.GameWorldSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.ResourceSystem;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.systems.StateMachineSystem;
import com.gdxjam.systems.SteeringSystem;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;

public class GameScreen extends AbstractScreen {

	private GameWorld world;

	private PooledEngine engine;
	private PhysicsSystem physicsSystem;
	private SteeringSystem steeringSystem;
	private GUISystem gui;
	private CameraSystem cameraSystem;

	// private Squad squadA;
	// private Squad squadB;

	@Override
	public void show() {
		super.show();

		initEngine();
		world = createTestWorld();
		loadWorld(world);

		initGUI();
		DesktopInputProcessor input = new DesktopInputProcessor(engine);
		Gdx.input.setInputProcessor(input);
	}

	public GameWorld createTestWorld() {
		GameWorld world = new GameWorld(64, 36);

//		Squad squadA = EntityFactory.createSquad(new Vector2(0, 10));
//		Squad squadB = EntityFactory.createSquad(new Vector2(10, 10));
//		Squad squadC = EntityFactory.createSquad(new Vector2(0, 0));
//		Squad squadD = EntityFactory.createSquad(new Vector2(10, 0));

		EntityFactory.createFortress(new Vector2(10, 10), 12, 12);
		return world;
	}

	public void initGUI() {
		/**
		 * The gui viewport needs to be set to something consistent
		 * */
		gui = new GUISystem(640, 360, engine);
		engine.addSystem(gui);
	}

	public void updateGUI(GameWorld world, float deltaTime) {

		if (Constants.pausedGUI != gui.isPaused) {
			gui.setPaused(Constants.pausedGUI);
		}

		gui.foodLabel.setText("Food: " + world.food + " / "
				+ ResourceSystem.foodThreshold);
	}


	public PooledEngine initEngine () {
		engine = new PooledEngine();
		EntityFactory.setEngine(engine);

		cameraSystem = new CameraSystem(64, 36);
		cameraSystem.getCamera().position.set(32, 18, 0);
		engine.addSystem(cameraSystem);

		physicsSystem = new PhysicsSystem();
		engine.addSystem(physicsSystem);

		steeringSystem = new SteeringSystem();
		engine.addSystem(steeringSystem);

		engine.addSystem(new EntityRenderSystem(cameraSystem.getCamera()));

		SquadSystem squadSystem = new SquadSystem();
		engine.addSystem(squadSystem);
		
		SteeringSystem steeringSystem = new SteeringSystem();
		
		//AI
		engine.addSystem(steeringSystem);
		engine.addSystem(new StateMachineSystem());
		engine.addSystem(new SquadSystem());
		
		engine.addSystem(new EntityRenderSystem(cameraSystem.getCamera()));
		return engine;
	}

	public void loadWorld(GameWorld world) {
		engine.addSystem(new ResourceSystem(world));
		engine.addSystem(new GameWorldSystem(world));
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		gui.resize(width, height);
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glClearColor(0, 0, 0, 1);

		updateGUI(world, delta);
		engine.update(delta);

	}

	@Override
	public void pause() {
		super.pause();
		gui.pause();

		// for (Map map : maps) {
		// map.save(map.getKey());
		// }
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		gui.resume();
	}

}
