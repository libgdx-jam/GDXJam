package com.gdxjam.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.GameManager;
import com.gdxjam.ecs.EntityManager;
import com.gdxjam.input.DesktopGestureListener;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.GUISystem;
import com.gdxjam.systems.PauseOverlay;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.WorldGenerator;

public class GameScreen extends AbstractScreen {

	private EntityManager engine;
	private PauseOverlay pauseOverlay;
	private InputMultiplexer multiplexer;

	@Override
	public void show() {
		engine = GameManager.initEngine();
		createWorld(256, 256);
		pauseOverlay = new PauseOverlay();

		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(engine.getSystem(GUISystem.class).getStage());
		multiplexer.addProcessor(new DesktopInputProcessor(engine));
		multiplexer.addProcessor(new GestureDetector(
				new DesktopGestureListener(engine)));
		multiplexer.addProcessor(pauseOverlay.getStage());
		Gdx.input.setInputProcessor(multiplexer);

	}

	public void createWorld(int width, int height) {
		long seed = new Random().nextLong();
		Vector2 center = new Vector2(width * 0.5f, height * 0.5f);
		WorldGenerator generator = new WorldGenerator(width, height, seed);
		generator.generate();

		engine.getSystem(CameraSystem.class).getCamera().position.set(
				width * 0.5f, height * 0.5f, 0);
		engine.getSystem(CameraSystem.class).setWorldBounds(width, height);

	}

	public void createTestEnemies() {
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (!Constants.isPaused) {
			engine.update(delta);
			Gdx.input.setInputProcessor(multiplexer);
		} else {
			pauseOverlay.render(delta);
			Gdx.input.setInputProcessor(pauseOverlay.getStage());
		}
	}

	@Override
	public void dispose() {
		pauseOverlay.dispose();

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
		pauseOverlay.resize(width, height);
		engine.getSystem(GUISystem.class).resize(width, height);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

}
