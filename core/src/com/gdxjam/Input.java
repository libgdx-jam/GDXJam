package com.gdxjam;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gdxjam.components.PositionComponent;

public class Input implements InputProcessor {
	OrthographicCamera camera;
	Entity target;

	public Input(OrthographicCamera camera, Entity target) {
		this.camera = camera;
		this.target = target;
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
		Vector3 touch = camera.unproject(new Vector3(screenX, screenY, 0));
		camera.position.set(touch.x, touch.y, 0);
		camera.update();
		target.getComponent(PositionComponent.class).position = new Vector2(touch.x, touch.y);

		return false;
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

		return false;
	}
}
