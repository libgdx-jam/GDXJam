package com.gdxjam.test.assets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gdxjam.Assets;
import com.gdxjam.screens.AbstractScreen;

public class AssetPlaygroundScreen extends AbstractScreen {

	SpriteBatch batch;
	OrthographicCamera camera;

	@Override
	public void show() {
		super.show();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(20, 20);
		camera.position.set(10, 10, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

	}

	@Override
	public void render(float delta) {
		super.render(delta);
		camera.update();
		batch.begin();
		batch.draw(Assets.getInstance().minimal.unit, 5, 5, 1, 1);
		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
