package com.gdxjam.screens;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.Assets;
import com.gdxjam.Input;
import com.gdxjam.components.MovementComponent;
import com.gdxjam.components.PositionComponent;
import com.gdxjam.components.VisualComponent;
import com.gdxjam.map.GameMapPixMap;
import com.gdxjam.map.Map;
import com.gdxjam.systems.RenderSystem;
import com.gdxjam.systems.UpdateSystem;

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

		batch = new SpriteBatch();
		camera = new OrthographicCamera(map.size, map.size);
		camera.update();
		camera.position.set(map.size / 2, map.size / 2, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		engine = new PooledEngine();
		engine.addSystem(new RenderSystem(camera));
		engine.addSystem(new UpdateSystem());

		Entity e = new Entity();
		e.add(new VisualComponent(Assets.instance.chest.reg));
		e.add(new PositionComponent(11, 11));
		e.add(new MovementComponent());
		e.getComponent(MovementComponent.class).entity
				.setMaxLinearAcceleration(10);
		e.getComponent(MovementComponent.class).entity.setMaxLinearSpeed(10);
		e.getComponent(MovementComponent.class).entity
				.setMaxAngularAcceleration(40);
		e.getComponent(MovementComponent.class).entity.setMaxAngularSpeed(10);

		Entity f = new Entity();
		f.add(new VisualComponent(Assets.instance.chest.open));
		f.add(new PositionComponent(8, 8));
		f.add(new MovementComponent());

		final Arrive<Vector2> arriveSB = new Arrive<Vector2>(
				e.getComponent(MovementComponent.class).entity,
				f.getComponent(MovementComponent.class).entity) //
				.setTimeToTarget(0.1f) //
				.setArrivalTolerance(0.001f) //
				.setDecelerationRadius(80);

		e.getComponent(MovementComponent.class).entity
				.setSteeringBehavior(arriveSB);

		map.addToAshley(engine);
		engine.addEntity(e);
		engine.addEntity(f);

		input = new Input(camera, f);
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
