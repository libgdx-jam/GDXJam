package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdxjam.EntityManager;
import com.gdxjam.GameWorld;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.systems.HUDSystem;
import com.gdxjam.ui.HotkeyTable;

public class AssetPlaygroundScreen extends AbstractScreen {

	SpriteBatch batch;
	OrthographicCamera camera;
	Stage stage;
	Viewport viewport;

	HUDSystem hud;
	HotkeyTable hotkey;

	@Override
	public void show() {
		super.show();
		EntityManager.getInstance().initSystems();
		EntityManager.getInstance().loadWorld(new GameWorld(64, 36));
		hud = EntityManager.getInstance().getSystem(HUDSystem.class);
		hotkey = hud.getHotkeyTable();
		hotkey.addHotkey(Keys.Q, "Q");
		hotkey.addHotkey(Keys.W, "W");
		hotkey.addHotkey(Keys.E, "E");
		hotkey.addHotkey(Keys.R, "R");
		hotkey.addHotkey(Keys.T, "T");

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(hud.getStage());
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
