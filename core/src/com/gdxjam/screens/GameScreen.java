package com.gdxjam.screens;

import java.util.Random;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.gdxjam.GameManager;
import com.gdxjam.GameManager.GameConfig;
import com.gdxjam.GameManager.GameConfig.BUILD;
import com.gdxjam.ecs.EntityManager;
import com.gdxjam.input.DesktopGestureListener;
import com.gdxjam.input.DeveloperInputProcessor;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.GUISystem;
import com.gdxjam.systems.InputSystem;
import com.gdxjam.utils.WorldGenerator;
import com.gdxjam.utils.WorldGenerator.WorldGeneratorParameter;

public class GameScreen extends AbstractScreen {

	private EntityManager engine;
	private InputMultiplexer multiplexer;

	public GameScreen() {
	}

	@Override
	public void show() {
		engine = GameManager.initEngine();
		createWorld(256, 256);;
		
		multiplexer = engine.getSystem(InputSystem.class).getMultiplexer();
		multiplexer.addProcessor(engine.getSystem(GUISystem.class).getStage());
		multiplexer.addProcessor(new GestureDetector(
				new DesktopGestureListener(engine)));

		if (GameConfig.build == BUILD.DEV) {
			multiplexer.addProcessor(new DeveloperInputProcessor());
		}

	}

	public void createWorld(int width, int height) {
		long seed = new Random().nextLong();
		WorldGeneratorParameter param = new WorldGeneratorParameter();
		param.initalSquads = 5;
		param.squadMembers = 9;
		WorldGenerator generator = new WorldGenerator(width, height, seed,
				param);
		generator.generate();

		engine.getSystem(CameraSystem.class).getCamera().position.set(
				width * 0.5f, height * 0.5f, 0);
		engine.getSystem(CameraSystem.class).setWorldBounds(width, height);

	}

	@Override
	public void render(float delta) {
		super.render(delta);
		engine.update(delta);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
		engine.getSystem(GUISystem.class).resize(width, height);
	}

	@Override
	public void hide() {
	}

}
