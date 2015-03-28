package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;
import com.gdxjam.GameManager.GameConfig;
import com.gdxjam.utils.Constants;

public class SplashScreen extends AbstractScreen {

	Texture logo;
	Texture gamelogo, outpostLogo;
	float alpha = 0;

	Stage stage;
	Table table;

	@Override
	public void show() {
		logo = new Texture(Gdx.files.internal("logo.png"));
		gamelogo = new Texture(Gdx.files.internal("gamelogospin.png"));
		outpostLogo = new Texture(Gdx.files.internal("outpostgamelogo.png"));
		outpostLogo.setFilter(TextureFilter.Nearest, TextureFilter.Linear);

		stage = new Stage();
		table = new Table();
		table.setFillParent(true);
		// table.debug();

		Image outpost = new Image(outpostLogo);
		outpost.setOrigin(outpost.getWidth() / 2, outpost.getHeight() / 2);
		outpost.addAction(new Fade());
		outpost.addAction(new Action() {
			@Override
			public boolean act(float delta) {
				getActor().rotateBy(-5f * delta);
				return false;
			}
		});

		Image rion = new Image(gamelogo);
		rion.addAction(new Fade());
		Image gdxjam = new Image(logo);
		gdxjam.addAction(new Fade());

		// Add everything to stage
		table.add(outpost);
		table.add(rion);
		table.row();
		table.add(gdxjam).colspan(2);
		stage.addActor(table);
		Assets.load();
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		switch (GameConfig.build) {

		default:
		case RELEASE:
			if (alpha >= 1) {
				if (Assets.getManager().update()) {
					Assets.create();
					GameManager.setScreen(new MainMenuScreen());
				}
			}
			break;

		case DEV:
			alpha = 1;
			if (Assets.getManager().update()) {
				Assets.create();
				GameManager.setScreen(new MainMenuScreen());
			}
			break;
		}

		// Four seconds to complete fade in
		if (alpha >= 1)
			alpha = 1;
		else
			alpha += 0.25f * delta;

		stage.act();
		stage.draw();

	}

	@Override
	public void dispose() {
		stage.dispose();
		logo.dispose();
		gamelogo.dispose();
		outpostLogo.dispose();
	}

	public class Fade extends Action {

		@Override
		public boolean act(float delta) {
			getActor().setColor(1, 1, 1, alpha);
			return false;
		}

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
