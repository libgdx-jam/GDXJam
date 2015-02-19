package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdxjam.Assets;
import com.gdxjam.ai.Squad;
import com.gdxjam.utils.Constants;

public class GUISystem extends EntitySystem {

	private OrthographicCamera camera;
	private Viewport viewport;
	private Stage stage;
	Array<Squad> squads;

	Array<TextButton> hotkeys = new Array<TextButton>(10);

	// GUI
	public Label foodLabel;

	public boolean isPaused = false;

	public GUISystem(float viewportWidth, float viewportHeight,
			PooledEngine engine) {
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
		viewport = new ScalingViewport(Scaling.stretch, viewportWidth,
				viewportHeight, camera);
		stage = new Stage(viewport);
		this.squads = engine.getSystem(SquadSystem.class).getSquads();
		init();
	}

	public void init() {
		Skin skin = Assets.getManager().get(Assets.SKIN, Skin.class);
		foodLabel = new Label("Food: 0 / 0", skin);
		// foodLabel.setPosition(camera.viewportWidth - 100,
		// camera.viewportHeight - 50);
		// foodLabel.setSize(3, 1);

		Table table = new Table();
		table.setFillParent(true);
		table.defaults().pad(10);
		table.add(foodLabel);

		stage.addActor(table);
		table.top().right();

		stage.addActor(foodLabel);

		Table bottomHotkeys = new Table();
		bottomHotkeys.defaults().pad(10);

		for (int x = 0; x < squads.size; x++) {
			TextButton btn = new TextButton(x + 1 + "", skin);
			bottomHotkeys.add(btn);
			hotkeys.add(btn);

		}

		bottomHotkeys.setPosition(
				camera.viewportWidth / 2 - bottomHotkeys.getWidth(), 10);

		stage.addActor(bottomHotkeys);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		for (int x = 0; x < squads.size; x++) {
			if (squads.get(x).isSelected()) {
				hotkeys.get(x).setColor(Color.GREEN);
			} else {
				hotkeys.get(x).setColor(Color.GRAY);
			}
		}

		camera.update();
		stage.act();
		stage.draw();
	}

	public void setPaused(boolean isPaused) {
		if (isPaused != this.isPaused) {
			if (isPaused) {
				pause();
			} else {
				resume();
			}
		}
		this.isPaused = !this.isPaused;

	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height);

	}

	public OrthographicCamera getCamera() {
		return (OrthographicCamera) viewport.getCamera();
	}

	public Viewport getViewport() {
		return viewport;
	}

	public void dispose() {
		stage.dispose();
	}

	public void pause() {
		Constants.pausedGUI = true;
		for (Actor actor : stage.getActors()) {
			actor.setVisible(false);
		}
	}

	public void resume() {
		Constants.pausedGUI = false;
		for (Actor actor : stage.getActors()) {
			actor.setVisible(true);
		}
	}

}
