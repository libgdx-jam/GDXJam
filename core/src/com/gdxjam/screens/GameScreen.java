package com.gdxjam.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.DesktopInputProcessor;
import com.gdxjam.GameWorld;
import com.gdxjam.ai.Squad;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.EntityRenderSystem;
import com.gdxjam.systems.GUISystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.ResourceSystem;
import com.gdxjam.systems.SquadSystem;
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
	private SquadSystem squads;

	// private Squad squadA;
	// private Squad squadB;

	@Override
	public void show() {
		super.show();

		initEngine();
		world = createTestWorld();
		loadWorld(world);

		initGUI();

		DesktopInputProcessor input = new DesktopInputProcessor(engine, world);
		Gdx.input.setInputProcessor(input);
	}

	public GameWorld createTestWorld() {
		GameWorld world = new GameWorld();

		Squad squadA = createSquad(new Vector2(0, 10));
		Squad squadB = createSquad(new Vector2(10, 10));
		Squad squadC = createSquad(new Vector2(0, 0));
		Squad squadD = createSquad(new Vector2(10, 0));

		squads.add(squadA);
		squads.add(squadB);
		squads.add(squadC);
		squads.add(squadD);

		EntityFactory.createFortress(new Vector2(10, 10), 12, 12);
		return world;
	}

	public Squad createSquad(Vector2 position) {
		Squad squad = new Squad(position);
		position.set(position.x - 1, position.y - 1);
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (x == 1 && y == 1) {
					squad.addMember(EntityFactory.createCommander(new Vector2(
							position.x + x, position.y + y)));
				} else {
					squad.addMember(EntityFactory.createUnit(new Vector2(
							position.x + x, position.y + y)));
				}
			}
		}
		return squad;
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

	public void initEngine() {
		engine = new PooledEngine();
		EntityFactory.setEngine(engine);

		cameraSystem = new CameraSystem(new OrthographicCamera(64, 36));
		engine.addSystem(cameraSystem);

		physicsSystem = new PhysicsSystem();
		engine.addSystem(physicsSystem);

		steeringSystem = new SteeringSystem();
		engine.addSystem(steeringSystem);

		engine.addSystem(new EntityRenderSystem(cameraSystem.getCamera()));

		squads = new SquadSystem();
		engine.addSystem(squads);
	}

	public void loadWorld(GameWorld world) {
		engine.addSystem(new ResourceSystem(world));
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
