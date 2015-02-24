package com.gdxjam.test.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.EntityManager;
import com.gdxjam.GameManager;
import com.gdxjam.input.DefaultInputProcessor;
import com.gdxjam.screens.AbstractScreen;
import com.gdxjam.utils.EntityFactory;

public class AssetPlaygroundScreen extends AbstractScreen {

	private EntityManager engine;

	@Override
	public void show() {
		super.show();

		engine = GameManager.initEngine();
		createWorld();
		Gdx.input.setInputProcessor(new DefaultInputProcessor());
	}

	public void createWorld() {
		EntityFactory.createMothership(new Vector2(32, 18));
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		engine.update(delta);
	}
}
