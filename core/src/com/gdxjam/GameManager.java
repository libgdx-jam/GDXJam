package com.gdxjam;

import com.badlogic.gdx.Game;
import com.gdxjam.screens.AbstractScreen;

public class GameManager {
	
	private static Game game;
	
	public static void init(Game game){
		GameManager.game = game;
	}
	
	public static void setScreen(AbstractScreen screen){
		if(game.getScreen() != null){
			game.getScreen().dispose();	
		}
		
		game.setScreen(screen);
	}

}
