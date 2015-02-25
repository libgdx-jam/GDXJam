package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.EntityManager;
import com.gdxjam.GameManager;
import com.gdxjam.input.DefaultInputProcessor;
import com.gdxjam.input.DesktopGestureListener;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.systems.HUDSystem;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.generators.WorldGenerator;

public class GameScreen extends AbstractScreen {

	private EntityManager engine;

	@Override
	public void show() {
		super.show();

		engine = GameManager.initEngine();
		createWorld();
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(engine.getSystem(HUDSystem.class).getStage());
		multiplexer.addProcessor(new DefaultInputProcessor());
		multiplexer.addProcessor(new DesktopInputProcessor(engine));
		multiplexer.addProcessor(new GestureDetector(new DesktopGestureListener(engine)));
		Gdx.input.setInputProcessor(multiplexer);

	}

	public void createWorld() {
		EntityFactory.createOutpost(new Vector2(32, 18));
		WorldGenerator.generateAsteroidField(new Vector2(0, 20), 25, 25, 0.4f, 1.2f, 0.1f);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		engine.update(delta);
	}

}
