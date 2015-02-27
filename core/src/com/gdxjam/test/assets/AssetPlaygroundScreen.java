package com.gdxjam.test.assets;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
	public void show() {
		super.show();
		engine = GameManager.initEngine();

		engine.getSystem(InputSystem.class).add(new DefaultInputProcessor(),
				new DesktopInputProcessor(engine),
				new GestureDetector(new DesktopGestureListener(engine)));

		Sprite sprite = new Sprite(Assets.space.largePlanetGreen);
		sprite.setPosition(10, 10);
		sprite.setSize(10, 10);

		Sprite sprite2 = new Sprite(Assets.space.largePlanetRed);
		sprite2.setPosition(20, 20);
		sprite2.setSize(10, 10);

		Sprite space = new Sprite(Assets.space.space);
		space.setBounds(Constants.WORLD_WIDTH_METERS / 2,
				Constants.WORLD_HEIGHT_METERS / 2,
				Constants.WORLD_WIDTH_METERS, Constants.WORLD_HEIGHT_METERS);
		PolygonShape spaceShape = new PolygonShape();
		spaceShape.setAsBox(Constants.WORLD_WIDTH_METERS / 2,
				Constants.WORLD_HEIGHT_METERS / 2);

		// Order matters first in the back
		EntityFactory.createEntity(space, spaceShape);
		EntityFactory.createCircleEntity(sprite);
		EntityFactory.createCircleEntity(sprite2);

	}

	@Override
	public void render(float delta) {
		super.render(delta);
		engine.update(delta);

	}

}
