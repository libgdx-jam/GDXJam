package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
		init(engine);
	}

	public void init(PooledEngine engine) {
		this.squads = engine.getSystem(SquadSystem.class).getSquads();

		LabelStyle labelStyle = new LabelStyle(
				Assets.getInstance().fonts.small, Color.WHITE);
		foodLabel = new Label("Food: 0 / 0", labelStyle);

		Table table = new Table();
		table.setFillParent(true);
		table.defaults().pad(10);
		table.add(foodLabel);

		stage.addActor(table);
		table.top().right();

		stage.addActor(table);

		Table bottomHotkeys = new Table();
		ImageButton left = new ImageButton(new TextureRegionDrawable(
				Assets.getInstance().hotkey.left));
		bottomHotkeys.add(left);

		for (int x = 0; x < squads.size; x++) {
			NinePatchDrawable draw = new NinePatchDrawable(
					Assets.getInstance().hotkey.button);

			TextButtonStyle style = new ImageTextButtonStyle();
			style.up = draw;
			style.down = draw.tint(Color.GREEN);
			style.checked = draw;
			style.font = Assets.getInstance().fonts.medium;

			TextButton btn = new TextButton(x + 1 + "", style);

			bottomHotkeys.add(btn);
			hotkeys.add(btn);

			if (x < squads.size - 1) {
				ImageButton middle = new ImageButton(new TextureRegionDrawable(
						Assets.getInstance().hotkey.middle));
				bottomHotkeys.add(middle);
			}

		}

		ImageButton right = new ImageButton(new TextureRegionDrawable(
				Assets.getInstance().hotkey.right));
		bottomHotkeys.add(right);

		bottomHotkeys.setPosition(
				camera.viewportWidth / 2 - bottomHotkeys.getWidth(), 25);

		// only add the bottom hotbar if there is something there to add
		if (squads.size >= 1)
			stage.addActor(bottomHotkeys);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		for (int x = 0; x < hotkeys.size; x++) {
			if (squads.get(x).isSelected()) {
				hotkeys.get(x).setColor(Color.DARK_GRAY);
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
