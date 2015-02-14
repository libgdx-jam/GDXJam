package com.gdxjam.ai.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.gdxjam.Assets;

public class GuysScreen implements Screen {

	SpriteBatch batch;
	OrthographicCamera camera;
	SteerableGuy dude, target;

	@Override
	public void show() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(50, 50);
		batch.setProjectionMatrix(camera.combined);

		dude = new SteerableGuy(true);
		dude.setPosition(20, 20);
		dude.setMaxLinearAcceleration(100);
		dude.setMaxLinearSpeed(100);
		dude.setMaxAngularAcceleration(40);
		dude.setMaxAngularSpeed(15);

		target = new SteerableGuy(true);
		target.setPosition(10, 10);

		final LookWhereYouAreGoing<Vector2> lookWhereYouAreGoingSB = new LookWhereYouAreGoing<Vector2>(
				dude) //
				.setTimeToTarget(0.1f) //
				.setAlignTolerance(0.001f) //
				.setDecelerationRadius(MathUtils.PI);

		final Arrive<Vector2> arriveSB = new Arrive<Vector2>(dude, target) //
				.setTimeToTarget(0.1f) //
				.setArrivalTolerance(0.001f) //
				.setDecelerationRadius(80);

		BlendedSteering<Vector2> blendedSteering = new BlendedSteering<Vector2>(
				dude) //
				.setLimiter(NullLimiter.NEUTRAL_LIMITER) //
				.add(arriveSB, 1f) //
				.add(lookWhereYouAreGoingSB, 1f);
		dude.setSteeringBehavior(blendedSteering);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		dude.update(delta);
		target.update(delta);
		batch.begin();
		batch.draw(Assets.instance.grass.reg, dude.getPosition().x,
				dude.getPosition().x, 1, 1);
		batch.draw(Assets.instance.post.post1, target.getPosition().x,
				dude.getPosition().x, 1, 1);
		batch.end();
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
