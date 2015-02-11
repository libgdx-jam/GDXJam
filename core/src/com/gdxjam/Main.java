package com.gdxjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.gdxjam.screens.GameScreen;
import com.gdxjam.screens.ScreenManager;

public class Main extends Game {

	@Override
	public void create() {
		Assets.instance.init(new AssetManager());
		ScreenManager.instance.init(this);
		ScreenManager.instance.setScreen(ScreenManager.instance.getDefault());
	}
}
