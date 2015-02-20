package com.gdxjam;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.Squad;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.ScreenshotFactory;

public class DesktopInputProcessor implements InputProcessor {

	OrthographicCamera camera;
	Array<Squad> squads;

	private GameWorld world;

	public DesktopInputProcessor(PooledEngine engine) {
		this.camera = engine.getSystem(CameraSystem.class).getCamera();
		this.squads = engine.getSystem(SquadSystem.class).getSquads();
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
		if (button == Buttons.LEFT) {
			for (Squad squad : squads) {
				if (squad.isSelected()) {
					squad.setTarget(pos.x, pos.y);
				}
			}
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
		// Add keys to select platoons
		case Constants.HOTKEY_PLATOON1:
			squads.get(0).setSelected(!squads.get(0).isSelected());
			return true;
		case Constants.HOTKEY_PLATOON2:
			squads.get(1).setSelected(!squads.get(1).isSelected());
			return true;
		case Constants.HOTKEY_PLATOON3:
			squads.get(2).setSelected(!squads.get(2).isSelected());
			return true;
		case Constants.HOTKEY_PLATOON4:
			squads.get(3).setSelected(!squads.get(3).isSelected());

			return true;

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
