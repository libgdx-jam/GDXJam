
package com.gdxjam.test.assets;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;
import com.gdxjam.input.DefaultInputProcessor;
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
	public void show () {
		super.show();
		engine = GameManager.initEngine();

		engine.getSystem(InputSystem.class).add(new DefaultInputProcessor(), new DesktopInputProcessor(engine),
			new GestureDetector(new DesktopGestureListener(engine)));

// Sprite sprite = new Sprite(Assets.space.largePlanetGreen);
// sprite.setPosition(10, 10);
// sprite.setSize(10, 10);

// Sprite sprite2 = new Sprite(Assets.space.largePlanetRed);
// sprite2.setPosition(20, 20);
// sprite2.setSize(10, 10);

// Sprite space = new Sprite(Assets.space.space);
// space.setBounds(Constants.WORLD_WIDTH_METERS / 2,
// Constants.WORLD_HEIGHT_METERS / 2,
// Constants.WORLD_WIDTH_METERS, Constants.WORLD_HEIGHT_METERS);

		// XXX: Background assets should not have physics

// PolygonShape spaceShape = new PolygonShape();
// spaceShape.setAsBox(Constants.WORLD_WIDTH_METERS / 2,
// Constants.WORLD_HEIGHT_METERS / 2);

		// TODO Add Z sorting to the spriteRenderer
// // Order matters first in the back
// EntityFactory.createEntity(space, spaceShape);
// EntityFactory.createCircleEntity(sprite);
// EntityFactory.createCircleEntity(sprite2);

		EntityFactory.createBackgroundArt(new Vector2((Constants.WORLD_WIDTH_METERS / 2) - (Constants.WORLD_WIDTH_METERS / 2),
			(Constants.WORLD_HEIGHT_METERS / 2) - (Constants.WORLD_HEIGHT_METERS / 2)), Constants.WORLD_WIDTH_METERS,
			Constants.WORLD_HEIGHT_METERS, Assets.space.space);
		EntityFactory.createBackgroundArt(new Vector2(10, 10), 10, 10, Assets.space.largePlanetGreen);
		EntityFactory.createBackgroundArt(new Vector2(20, 20), 10, 10, Assets.space.largePlanetRed);
	}

	@Override
	public void render (float delta) {
		super.render(delta);
		engine.update(delta);

	}

}
