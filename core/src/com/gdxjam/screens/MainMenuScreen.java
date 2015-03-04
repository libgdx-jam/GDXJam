package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;
import com.gdxjam.utils.Constants;

public class MainMenuScreen extends AbstractScreen {
	Stage stage;
	Table table;

	@Override
	public void show() {
		stage = new Stage();
		table = new Table();
		table.setFillParent(true);
		table.defaults().width(Gdx.graphics.getWidth() / 2).pad(20);

		addTitle();
		add("New Game", new GameScreen());
		add("Load Game", new LoadGameScreen());
		add("Settings", new OptionsScreen());
		addExit();

		stage.addActor(table);

		Gdx.input.setInputProcessor(stage);
	}

	private void addTitle() {
		Label title = new Label(Constants.GAME_TITLE, Assets.skin);
		title.setAlignment(Align.center);
		table.add(title);
		table.row();
	}

	private void addExit() {
		NinePatchDrawable draw = new NinePatchDrawable(Assets.hotkey.button);

		TextButtonStyle style = new ImageTextButtonStyle();
		style.up = draw;
		style.down = draw.tint(Color.DARK_GRAY);
		style.checked = draw;
		style.font = Assets.fonts.font;

		TextButton btn = new TextButton("Exit", style);
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.exit();
			}
		});
		table.add(btn);
		table.row();
	}

	public void add(String title, final AbstractScreen screen) {
		NinePatchDrawable draw = new NinePatchDrawable(Assets.hotkey.button);

		TextButtonStyle style = new ImageTextButtonStyle();
		style.up = draw;
		style.down = draw.tint(Color.DARK_GRAY);
		style.checked = draw;
		style.font = Assets.fonts.font;

		TextButton btn = new TextButton(title, style);
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				GameManager.setScreen(screen);
			}
		});
		table.add(btn);
		table.row();

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		stage.draw();
		stage.act();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

}
