package com.gdxjam.test.assets;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;
import com.gdxjam.screens.AbstractScreen;
import com.gdxjam.utils.EntityFactory;

public class AssetPlaygroundScreen extends AbstractScreen {
	Engine engine;

	@Override
	public void show() {
		super.show();
		engine = GameManager.initEngine();
		Sprite sprite = new Sprite(Assets.planet.largePlanetGreen);
		sprite.setPosition(10, 10);
		sprite.setSize(10, 10);

		Sprite sprite2 = new Sprite(Assets.planet.largePlanetRed);
		sprite2.setPosition(20, 20);
		sprite2.setSize(10, 10);

		EntityFactory.createCircleEntity(sprite);
		EntityFactory.createCircleEntity(sprite2);

	}

	@Override
	public void render(float delta) {
		super.render(delta);
		engine.update(delta);

	}

}
