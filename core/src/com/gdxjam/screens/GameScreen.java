package com.gdxjam.screens;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.gdxjam.Assets;
import com.gdxjam.components.PositionComponent;
import com.gdxjam.components.VisualComponent;
import com.gdxjam.map.GameMapPixMap;
import com.gdxjam.map.Map;
import com.gdxjam.systems.RenderSystem;
import com.gdxjam.tiles.Tile;
import com.gdxjam.utils.Constants.BLOCK_TYPE;

public class GameScreen implements Screen {

	ArrayList<Map> maps = new ArrayList<Map>();
	GameMapPixMap map;

	SpriteBatch batch;
	OrthographicCamera camera;
	InputProcessor input;

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

		maps.add(map);
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
		e.add(new VisualComponent(Assets.instance.chest.reg));
		e.add(new PositionComponent(11, 11));
		engine.addEntity(e);

		input = new InputProcessor() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {

				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {

				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				Vector3 touch = camera
						.project(new Vector3(screenX, screenY, 0));
				camera.position.set(touch.x, touch.y, 0);
				camera.update();

				System.out.println("DERPING!!!!!!");

				return false;
			}

			@Override
			public boolean scrolled(int amount) {

				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {

				return false;
			}

			@Override
			public boolean keyUp(int keycode) {

				return false;
			}

			@Override
			public boolean keyTyped(char character) {

				return false;
			}

			@Override
			public boolean keyDown(int keycode) {

				return false;
			}
		};

		Gdx.input.setInputProcessor(input);

	}

	@Override
	public void render(float delta) {

		if (Gdx.input.isKeyJustPressed(Keys.UP)) {
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		camera.update();
		batch.begin();
		for (Tile tile : map.getTiles()) {

			for (BLOCK_TYPE data : tile.getTileData()) {

				switch (data) {

				case FLOOR:
					batch.draw(Assets.instance.grass.reg, tile.getX(),
							tile.getY(), 1, 1);
					break;

				case POST1:
					batch.draw(Assets.instance.post.post1, tile.getX(),
							tile.getY(), 1, 1);
					break;

				case POST2:
					batch.draw(Assets.instance.post.post2, tile.getX(),
							tile.getY(), 1, 1);
					break;

				case EMPTY:
					break;

				default:
					batch.draw(Assets.instance.chest.reg, tile.getX(),
							tile.getY(), 1, 1);
					break;

				}
			}

		}
		batch.end();

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
