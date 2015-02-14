package com.gdxjam.screens;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
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

		initEngine();
		createTestEntity();
		
		input = new Input(camera);
		Gdx.input.setInputProcessor(input);
	}
	
	public void createTestEntity(){
		Entity entity = engine.createEntity();
		
		entity.add(engine.createComponent(VisualComponent.class).init(Assets.instance.chest.reg, 200));
		entity.add(engine.createComponent(PositionComponent.class).init(11, 11));

		engine.addEntity(entity);
	}
	
	
	public void initEngine(){
		engine = new PooledEngine();
		engine.addSystem(new RenderSystem(camera));
		map.addToAshley(engine);
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
