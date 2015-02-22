package com.gdxjam.test.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gdxjam.Assets;
import com.gdxjam.screens.AbstractScreen;

public class AssetPlaygroundScreen extends AbstractScreen {

	SpriteBatch batch;
	OrthographicCamera camera;
	ShaderProgram shader;

	@Override
	public void show() {
		super.show();

		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(
				Gdx.files.internal("shaders/grayscale.vert"),
				Gdx.files.internal("shaders/grayscale.frag"));
		if (!shader.isCompiled()) {
			System.err.println(shader.getLog());
			// System.exit(0);
		}
		if (shader.getLog().length() != 0)
			System.out.println(shader.getLog());

		batch = new SpriteBatch(1000, shader);
		batch.setShader(shader);
		batch.setShader(SpriteBatch.createDefaultShader());

		camera = new OrthographicCamera(200, 200);
		camera.position.set(100, 100, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		camera.update();
		batch.begin();
		// Grayscaled shader
		batch.setShader(shader);
		batch.draw(Assets.getInstance().planet.planet1, 100, 100, 100, 100);

		// default shader
		batch.setShader(SpriteBatch.createDefaultShader());
		batch.draw(Assets.getInstance().minimal.commander, 0, 0, 10, 10);
		batch.draw(Assets.getInstance().mothership.green, 50, 50, 50, 50);

		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		shader.begin();
		shader.setUniformf("resolution", width, height);
		shader.end();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		shader.dispose();
	}

}
