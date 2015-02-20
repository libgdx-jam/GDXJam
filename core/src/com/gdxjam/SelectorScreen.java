package com.gdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.gdxjam.screens.AbstractScreen;
import com.gdxjam.screens.GameScreen;
import com.gdxjam.screens.TestScreen2;

public class SelectorScreen extends AbstractScreen {

	Stage stage;
	Table table;

	@Override
	public void show() {
		super.show();
		stage = new Stage();
		table = new Table();
		table.setFillParent(true);
		table.defaults().width(Gdx.graphics.getWidth() / 2).pad(20);
		addScreen(new GameScreen());
		addScreen(new TestScreen2());

		stage.addActor(table);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		stage.draw();
		stage.act();

	}

	public void addScreen(final AbstractScreen screen) {
		NinePatchDrawable draw = new NinePatchDrawable(
				Assets.getInstance().hotkey.button);

		TextButtonStyle style = new ImageTextButtonStyle();
		style.up = draw;
		style.down = draw.tint(Color.DARK_GRAY);
		style.checked = draw;
		style.font = Assets.getInstance().fonts.medium;

		TextButton btn = new TextButton(screen.getClass().getSimpleName(),
				style);
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
		// TODO Auto-generated method stub
		super.dispose();

		stage.dispose();
	}

}
