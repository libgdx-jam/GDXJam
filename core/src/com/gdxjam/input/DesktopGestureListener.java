package com.gdxjam.input;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.systems.CameraSystem;

public class DesktopGestureListener implements GestureListener{
	
	private PooledEngine engine;
	private CameraSystem cameraSystem;
	
	public DesktopGestureListener (PooledEngine engine) {
		this.engine = engine;
		this.cameraSystem = engine.getSystem(CameraSystem.class);
	}

	@Override
	public boolean touchDown (float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap (float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress (float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean fling (float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan (float x, float y, float deltaX, float deltaY) {
		if(Gdx.input.isButtonPressed(Buttons.RIGHT)){
			cameraSystem.translate(-deltaX * 0.05f, deltaY * 0.05f);
		}
		return false;
	}

	@Override
	public boolean panStop (float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom (float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

}
