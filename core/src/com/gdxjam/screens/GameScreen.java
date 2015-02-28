package com.gdxjam.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.Assets;
import com.gdxjam.EntityManager;
import com.gdxjam.GameManager;
import com.gdxjam.input.DesktopGestureListener;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.HUDSystem;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.WorldGenerator;

public class GameScreen extends AbstractScreen {

	private EntityManager engine;

	@Override
	public void show() {
		super.show();

		engine = GameManager.initEngine();
		createWorld(256, 256);
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(engine.getSystem(HUDSystem.class).getStage());
		multiplexer.addProcessor(new DesktopInputProcessor(engine));
		multiplexer.addProcessor(new GestureDetector(new DesktopGestureListener(engine)));
		Gdx.input.setInputProcessor(multiplexer);

	}

	public void createWorld(int width, int height) {
		long seed = new Random().nextLong();
		Vector2 center = new Vector2(width * 0.5f, height * 0.5f);
		WorldGenerator generator = new WorldGenerator(width, height, seed);
		generator.generate();
		
		engine.getSystem(CameraSystem.class).getCamera().position.set(width * 0.5f, height * 0.5f, 0);
		engine.getSystem(CameraSystem.class).setWorldBounds(width, height);
		
		//Assets positions are defined as relative to the viewport
		
		EntityFactory.createBackgroundArt(new Vector2(0, 0), Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, Assets.space.background, 0);
	EntityFactory.createBackgroundArt(new Vector2(Constants.VIEWPORT_WIDTH / 4, Constants.VIEWPORT_HEIGHT / 4), 6, 6,
			Assets.space.planets.random(), 1);
//	EntityFactory.createBackgroundArt(new Vector2(20, 20), 10, 10,
//			Assets.space.largePlanetRed);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		engine.update(delta);
	}

}
