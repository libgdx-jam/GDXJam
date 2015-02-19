package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdxjam.Assets;
import com.gdxjam.utils.Constants;

public class GUISystem extends EntitySystem {

	private OrthographicCamera camera;
	private Viewport viewport;
	private Stage stage;

	// GUI
	public Label foodLabel;

	public boolean isPaused = false;

	public GUISystem(float viewportWidth, float viewportHeight) {
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
		viewport = new ScalingViewport(Scaling.stretch, viewportWidth,
				viewportHeight, camera);
		stage = new Stage(viewport);
		init();
	}

	public void init() {
		Skin skin = Assets.getManager().get(Assets.SKIN, Skin.class);
		foodLabel = new Label("Food: 0 / 0", skin);
		foodLabel.setPosition(camera.viewportWidth - 100,
				camera.viewportHeight - 50);
		foodLabel.setSize(3, 1);

		// Table table = new Table();
		// table.setFillParent(true);
		// table.defaults().pad(10);
		// table.add(foodLabel);
		//
		// stage.addActor(table);
		// table.top().right();
		stage.addActor(foodLabel);

		TextButton btnOne = new TextButton("1", skin);
		TextButton btnTwo = new TextButton("2", skin);
		TextButton btnThree = new TextButton("3", skin);
		TextButton btnFour = new TextButton("4", skin);

		Table bottomHotkeys = new Table();
		bottomHotkeys.defaults().pad(10);
		bottomHotkeys.add(btnOne, btnTwo, btnThree, btnFour);
		bottomHotkeys.setPosition(
				camera.viewportWidth / 2 - bottomHotkeys.getWidth(), 10);

		stage.addActor(bottomHotkeys);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

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
