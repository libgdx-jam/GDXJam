package com.gdxjam.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.gdxjam.Assets;

public class CreditsScreen extends AbstractScreen {

	Stage stage;
	Table table;

	@Override
	public void show() {
		stage = new Stage();
		table = new Table();
		table.setFillParent(true);
		LabelStyle labelStyle = new LabelStyle(Assets.fonts.font, new Color(1,
				1, 1, 1));

		Label label = new Label("Credits", labelStyle);

		table.add(label);
		stage.addActor(table);

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stage.act();
		stage.draw();

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
