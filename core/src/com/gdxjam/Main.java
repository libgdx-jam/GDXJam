package com.gdxjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.gdxjam.screens.GameScreen;
import com.gdxjam.screens.SelectorScreen;
import com.gdxjam.screens.TestScreen2;

public class Main extends Game {

	@Override
	public void create() {
		GameManager.init(this);
		GameManager.setScreen(new SelectorScreen());
	}

}
