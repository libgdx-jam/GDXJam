package com.gdxjam;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.gdxjam.ai.Squad;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.ScreenshotFactory;

public class DesktopInputProcessor implements InputProcessor {

	OrthographicCamera camera;

	private Squad battalionA;
	private Squad battalionB;
	private GameWorld world;

	public DesktopInputProcessor(OrthographicCamera camera, Squad battalionA,
			Squad battalionB, GameWorld world) {
		this.camera = camera;
		this.battalionA = battalionA;
		this.battalionB = battalionB;
		this.world = world;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		pos.set(camera.unproject(pos));
		// camera.position.set(touch.x, touch.y, 0);
		// camera.update();
		if (button == Buttons.LEFT) {
			battalionA.setTarget(pos.x, pos.y);
		} else {
			battalionB.setTarget(pos.x, pos.y);
		}
		return true;
	}

	@Override
	public boolean scrolled(int amount) {

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		
		//Add keys to select platoons
		
		case Keys.SPACE:
			Constants.pausedGUI = !Constants.pausedGUI;
			return true;
		case Keys.PLUS:
			world.food++;
			return true;
		case Keys.F12:
			ScreenshotFactory.saveScreenshot();
			return true;
		}

		return false;
	}

}
