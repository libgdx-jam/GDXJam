package com.gdxjam;

import com.badlogic.gdx.Game;
import com.gdxjam.screens.SplashScreen;

public class Main extends Game {

	@Override
	public void create() {
		GameManager.init(this);
		GameManager.setScreen(new SplashScreen());
	}

}
