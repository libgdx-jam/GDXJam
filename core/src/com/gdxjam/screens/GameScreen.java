package com.gdxjam.screens;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdxjam.Assets;
import com.gdxjam.Input;
import com.gdxjam.components.PositionComponent;
import com.gdxjam.components.VisualComponent;
import com.gdxjam.map.GameMapPixMap;
import com.gdxjam.map.Map;
import com.gdxjam.systems.RenderSystem;

public class GameScreen implements Screen {

	ArrayList<Map> maps = new ArrayList<Map>();
	GameMapPixMap map;

	SpriteBatch batch;
	OrthographicCamera camera;
	Input input;

	PooledEngine engine;

	@Override
	public void show() {

		map = new GameMapPixMap();
		map.setKey("test");
		map.convertPixmap("test.png");
		// map.save("test");
		// map.load("test");
		// Tile tile = new Tile(5, 5);
		// tile.addTileData(BLOCK_TYPE.POST2);
		// map.add(tile);

		// System.out.println("------Showing-----");

		batch = new SpriteBatch();
		camera = new OrthographicCamera(map.size, map.size);
		camera.update();
		camera.position.set(map.size / 2, map.size / 2, 0);
		camera.update();
		// cameraHelper = new CameraHelper(camera);
		batch.setProjectionMatrix(camera.combined);

		engine = new PooledEngine();
		engine.addSystem(new RenderSystem(camera));
		Entity e = new Entity();
		e.add(new VisualComponent(Assets.instance.chest.reg,200));
		e.add(new PositionComponent(11, 11));

		map.addToAshley(engine);
		engine.addEntity(e);

		input = new Input(camera);

			

		Gdx.input.setInputProcessor(input);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		engine.update(delta);

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
		for (Map map : maps) {
			map.save(map.getKey());
		}
	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
	}

}
