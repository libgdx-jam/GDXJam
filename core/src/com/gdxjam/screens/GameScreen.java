package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.EntityManager;
import com.gdxjam.GameManager;
import com.gdxjam.input.DefaultInputProcessor;
import com.gdxjam.input.DesktopGestureListener;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.HUDSystem;
import com.gdxjam.utils.AsteroidGenerator;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.generators.WorldGenerator;

public class GameScreen extends AbstractScreen {

	private EntityManager engine;

	@Override
	public void show() {
		super.show();

		engine = GameManager.initEngine();
		createWorld(128, 128);
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(engine.getSystem(HUDSystem.class).getStage());
		multiplexer.addProcessor(new DefaultInputProcessor());
		multiplexer.addProcessor(new DesktopInputProcessor(engine));
		multiplexer.addProcessor(new GestureDetector(new DesktopGestureListener(engine)));
		Gdx.input.setInputProcessor(multiplexer);

	}

	public void createWorld(int width, int height) {
		EntityFactory.createOutpost(new Vector2(width * 0.5f, height * 0.5f));
		AsteroidGenerator generator = new AsteroidGenerator(MathUtils.random(9999), 8, 64, width, height);
		generator.scatterAsteroids(width, height, 64, 64, 0.4f);
		engine.getSystem(CameraSystem.class).getCamera().position.set(width * 0.5f, height * 0.5f, 0);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		engine.update(delta);
	}

}
