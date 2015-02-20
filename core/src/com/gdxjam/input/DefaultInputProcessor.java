package com.gdxjam.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.gdxjam.EntityManager;
import com.gdxjam.GameManager;
import com.gdxjam.screens.SelectorScreen;

public class DefaultInputProcessor implements InputProcessor{

	@Override
	public boolean keyDown (int keycode) {
		switch(keycode){
			case Keys.ESCAPE:
				EntityManager.getInstance().dispose();
				GameManager.setScreen(new SelectorScreen());
				return true;
		}
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
