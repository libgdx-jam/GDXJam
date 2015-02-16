package com.gdxjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.gdxjam.screens.TestScreen;

public class Main extends Game {

	@Override
	public void create() {
		Assets.instance.init(new AssetManager());
		
		GameManager.init(this);
		GameManager.setScreen(new TestScreen());
	}
	
}
