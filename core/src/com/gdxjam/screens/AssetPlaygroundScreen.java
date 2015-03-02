
package com.gdxjam.screens;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.Assets;
import com.gdxjam.EntityManager;
import com.gdxjam.GameManager;
import com.gdxjam.input.DesktopGestureListener;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.screens.AbstractScreen;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.GUISystem;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.WorldGenerator;
import com.gdxjam.utils.WorldGenerator.WorldGeneratorParameter;

public class AssetPlaygroundScreen extends AbstractScreen {

	private EntityManager engine;

	@Override
	public void show () {
		super.show();

		engine = GameManager.initEngine();
		createWorld(256, 256);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(engine.getSystem(GUISystem.class).getStage());
		multiplexer.addProcessor(new DesktopInputProcessor(engine));
		multiplexer.addProcessor(new GestureDetector(new DesktopGestureListener(engine)));
		Gdx.input.setInputProcessor(multiplexer);

	}

	public void createWorld (int width, int height) {
		long seed = new Random().nextLong();
		WorldGeneratorParameter param = new WorldGeneratorParameter();
		param.generateBackground = false;
		WorldGenerator generator = new WorldGenerator(width, height, seed, param);
		generator.generate();

		engine.getSystem(CameraSystem.class).getCamera().position.set(width * 0.5f, height * 0.5f, 0);
		engine.getSystem(CameraSystem.class).setWorldBounds(width, height);

		// Assets positions are defined as relative to the viewport

		createBackground(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, 10);
	}

	public void createBackground (float width, float height, int planets) {
		EntityFactory.createBackgroundArt(new Vector2(0, 0), width, height, Assets.space.background, 0);
		Array<Entity> smallPlanets = new Array<Entity>();
		float radius;
		float maxRadius = 10;
		for (int x = 0; x < planets; x++) {
			radius = maxRadius * MathUtils.random();
			Entity e = EntityFactory.createBackgroundArt(new Vector2(width * MathUtils.random(), height * MathUtils.random()),
				radius, radius, Assets.space.planets.random(), (radius > maxRadius / 2) ? 1 : 2);
			if (!(radius > maxRadius / 2)) {
				smallPlanets.add(e);
			} else {
				engine.addEntity(e);
			}
		}

		for (Entity e : smallPlanets) {
			engine.addEntity(e);
		}
	}

	@Override
	public void render (float delta) {
		super.render(delta);

		engine.update(delta);
	}

}
