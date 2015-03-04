package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;
import com.gdxjam.utils.Constants;

public class SplashScreen extends AbstractScreen {

	SpriteBatch batch;
	Texture logo;
	float alpha = 0;

	@Override
	public void show() {
		super.show();
		batch = new SpriteBatch();
		logo = new Texture(Gdx.files.internal("logo.png"));
		Assets.load();
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		switch (Constants.build) {

		case RELEASE:
			if (alpha >= 1) {
				if (Assets.getManager().update()) {
					Assets.create();
					GameManager.setScreen(new MainMenuScreen());
				}
			}
			break;

		default:
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

		batch.begin();
		batch.setColor(1, 1, 1, alpha);
		batch.draw(logo, (Gdx.graphics.getWidth() / 2) - (logo.getWidth() / 2),
				(Gdx.graphics.getHeight() / 2) - (logo.getHeight() / 2));
		batch.end();
	}

}
