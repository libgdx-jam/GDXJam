package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;

public class SettingsScreen extends AbstractScreen {

	Preferences prefs;
	Stage stage;

	@Override
	public void show() {
		prefs = Gdx.app.getPreferences("My Preferences");
		stage = new Stage();
		Table table = new Table();
		table.debug();
		table.setFillParent(true);
		table.align(Align.top);
		table.defaults().pad(10);

		LabelStyle labelStyle = new LabelStyle(Assets.fonts.font, new Color(1,
				1, 1, 1));
		Label settingsLabel = new Label("Settings", labelStyle);
		settingsLabel.setFontScale(2);
		settingsLabel.setAlignment(Align.top);

		final Label volumeLabel = new Label("Volume", labelStyle);
		final Slider volumeSlider = new Slider(0, 100, 1, false, Assets.skin);
		volumeSlider.setAnimateDuration(0.3f);
		volumeSlider.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("UITest", "slider: " + volumeSlider.getValue());
				volumeLabel.setText("Volume: " + volumeSlider.getValue());
				prefs.putFloat("volume", volumeSlider.getValue());
			}
		});

		NinePatchDrawable draw = new NinePatchDrawable(Assets.hotkey.button);

		TextButtonStyle style = new ImageTextButtonStyle();
		style.up = draw;
		style.down = draw.tint(Color.DARK_GRAY);
		style.checked = draw;
		style.font = Assets.fonts.font;

		TextButton save = new TextButton("Save", style);
		save.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				prefs.flush();
				GameManager.setScreen(new MainMenuScreen());
			}
		});

		TextButton back = new TextButton("Back Without Saving", style);
		save.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				GameManager.setScreen(new MainMenuScreen());
			}
		});

		table.add(settingsLabel).colspan(6).row();
		table.add(volumeLabel);
		table.add(volumeSlider);
		table.row();
		table.add(save).colspan(6);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
