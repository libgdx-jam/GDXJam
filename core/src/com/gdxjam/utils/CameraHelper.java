package com.gdxjam.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {
	OrthographicCamera camera;

	Vector2 targetPosition;

	public CameraHelper(OrthographicCamera camera) {
		this.camera = camera;
	}

	public void set(float x, float y) {
		camera.position.set(x, y, 0);
	}

	public void follow(float x, float y) {
		set(x, y);
	}

}
