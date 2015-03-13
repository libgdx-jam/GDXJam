package com.gdxjam.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.gdxjam.utils.DeveloperTools;

public class DeveloperInputProcessor extends InputAdapter{
	
	@Override
	public boolean keyDown (int keycode) {
		switch(keycode){
		case Keys.F5:
			DeveloperTools.spawnEnemyAtCursor();
			return true;
		case Keys.F6:
			DeveloperTools.startWaveNow();
			return true;
		}
		return false;
	}

}
