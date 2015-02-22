package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdxjam.EntityManager;
import com.gdxjam.GameWorld;
import com.gdxjam.input.DefaultInputProcessor;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.systems.HUDSystem;

public class AssetPlaygroundScreen extends AbstractScreen {

	SpriteBatch batch;
	OrthographicCamera camera;
	Stage stage;
	Viewport viewport;

	@Override
	public void show() {
		super.show();
		EntityManager.getInstance().initSystems();
		EntityManager.getInstance().loadWorld(new GameWorld(64, 36));

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(EntityManager.getInstance()
				.getSystem(HUDSystem.class).getStage());
		multiplexer.addProcessor(new DefaultInputProcessor());
		multiplexer.addProcessor(new DesktopInputProcessor(EntityManager
				.getInstance()));
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		EntityManager.getInstance().update(delta);

	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
