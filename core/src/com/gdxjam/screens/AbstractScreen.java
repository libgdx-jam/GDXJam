package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public abstract class AbstractScreen implements Screen {

	public boolean paused = false;

	@Override
	public abstract void show();

	@Override
	public void render(float delta) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glClearColor(0, 0, 0, 1);
	}

	@Override
	public abstract void resize(int width, int height);

	@Override
	public abstract void pause();

	@Override
	public abstract void resume();

	@Override
	public abstract void hide();

	@Override
	public abstract void dispose();

}
