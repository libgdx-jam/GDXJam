package com.gdxjam;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.gdxjam.screens.AbstractScreen;

public class GameManager {
	
	private static Game game;
	
	public static void init(Game game){
		GameManager.game = game;
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}
	
	public static void setScreen(AbstractScreen screen){
		if(game.getScreen() != null){
			game.getScreen().dispose();	
		}
		
		game.setScreen(screen);
	}

}
