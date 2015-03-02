package com.gdxjam.input;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.GameManager;
import com.gdxjam.screens.SelectorScreen;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityUtils;

public class DesktopInputProcessor implements InputProcessor {

	private SquadSystem squadSystem;
	private CameraSystem cameraSystem;

	public DesktopInputProcessor(PooledEngine engine) {
		this.cameraSystem = engine.getSystem(CameraSystem.class);
		this.squadSystem = engine.getSystem(SquadSystem.class);
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
		if (button == Buttons.LEFT) {
			EntityUtils.setSelectedSquadTarget(cameraSystem.screenToWorldCords(screenX,
				screenY));
			return true;
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		cameraSystem.zoom(amount * 0.1f);
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
		/**
		 * Squad Hotkeys
		 */
		case Keybinds.SQUAD0:
			EntityUtils.toggleSelectedSquad(0);
			return true;
		case Keybinds.SQUAD1:
			EntityUtils.toggleSelectedSquad(1);
			return true;
		case Keybinds.SQUAD2:
			EntityUtils.toggleSelectedSquad(2);
			return true;
		case Keybinds.SQUAD3:
			EntityUtils.toggleSelectedSquad(3);
			return true;
		case Keybinds.SQUAD4:
			EntityUtils.toggleSelectedSquad(4);
			return true;

		/**
		 * Squad action groups
		 */
//		case Keybinds.ACTION0:
//			squadSystem.setState(UnitState.FORMATION);
//			return true;
//		case Keybinds.ACTION1:
//			squadSystem.setState(UnitState.COMBAT);
//			return true;
//		case Keybinds.ACTION2:
//			squadSystem.setState(UnitState.HARVEST);
//			return true;
		case Keybinds.ACTION3:
			return true;
		case Keybinds.ACTION4:
			return true;

		case Keys.SPACE:
			Constants.pausedGUI = !Constants.pausedGUI;
			return true;
		case Keys.F12:
			if(Gdx.app.getType() == ApplicationType.Desktop){
				//NOTE: Comment this out to run GWT
				//ScreenshotFactory.saveScreenshot();
			}
			return true;
		case Keys.ESCAPE:
			GameManager.disposeEngine();
			GameManager.setScreen(new SelectorScreen());
			return true;
			
		}

		return false;
	}

}
