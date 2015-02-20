package com.gdxjam;

import com.badlogic.gdx.Game;

public class Main extends Game {

	@Override
	public void create() {
		GameManager.init(this);
		GameManager.setScreen(new SelectorScreen());
	}

}
