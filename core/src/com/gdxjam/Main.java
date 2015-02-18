package com.gdxjam;

import com.badlogic.gdx.Game;
import com.gdxjam.screens.GameScreen;

public class Main extends Game {

	@Override
	public void create() {
		GameManager.init(this);
		Assets.getInstance();
		GameManager.setScreen(new GameScreen());
	}
	
}
