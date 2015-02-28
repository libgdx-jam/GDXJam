package com.gdxjam.test.assets;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;
import com.gdxjam.input.DesktopGestureListener;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.screens.AbstractScreen;
import com.gdxjam.systems.InputSystem;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;

public class AssetPlaygroundScreen extends AbstractScreen {
	PooledEngine engine;
	InputMultiplexer input;

	@Override
	public void show() {
		super.show();
		engine = GameManager.initEngine();

		engine.getSystem(InputSystem.class).add(
				new DesktopInputProcessor(engine),
				new GestureDetector(new DesktopGestureListener(engine)));

		EntityFactory.createBackgroundArt(new Vector2(0, 0),
				Constants.WORLD_WIDTH_METERS, Constants.WORLD_HEIGHT_METERS,
				Assets.space.space);
		EntityFactory.createBackgroundArt(new Vector2(10, 10), 10, 10,
				Assets.space.largePlanetGreen);
		EntityFactory.createBackgroundArt(new Vector2(20, 20), 10, 10,
				Assets.space.largePlanetRed);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		engine.update(delta);

	}

}
