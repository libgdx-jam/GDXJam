package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;

public class GameOverScreen extends AbstractScreen {

	float alpha = 0;
	Stage stage;

	@Override
	public void show() {
		stage = new Stage();
		Table table = new Table();
		table.setFillParent(true);
		table.align(Align.center);

		LabelStyle labelStyle = new LabelStyle(Assets.fonts.font, Color.RED);
		Label gameOver = new Label("GAME OVER", labelStyle);

		NinePatchDrawable draw = new NinePatchDrawable(Assets.hotkey.button);

		TextButtonStyle style = new ImageTextButtonStyle();
		style.up = draw;
		style.down = draw.tint(Color.DARK_GRAY);
		style.checked = draw;
		style.font = Assets.fonts.font;

		TextButton restart = new TextButton("Restart", style);
		restart.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				GameManager.setScreen(new NewGameScreen());
			}
		});

		TextButton back = new TextButton("Exit", style);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				GameManager.setScreen(new MainMenuScreen());
			}
		});

		table.add(gameOver).colspan(2).expandX();
		table.row().expandX();
		table.add(restart);
		table.add(back);
		stage.addActor(table);
		stage.addAction(new Fade());
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
		stage.getViewport().update(width, height);
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

	public class Fade extends Action {

		@Override
		public boolean act(float delta) {
			if (alpha < 1) {
				getActor().setColor(1, 1, 1, alpha);
				alpha += 0.25f * delta;
			}
			return false;
		}

	}

}
