package com.gdxjam;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.gdxjam.screens.AbstractScreen;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.EntityUtils;

public class GameManager {
	private static final String TAG = "[" + GameManager.class.getSimpleName() + "]";
	private static Game game;
	private static EntityManager engine;
	
	private static final int LOG_LEVEL = Application.LOG_DEBUG;
	
	public static void init(Game game){
		GameManager.game = game;
		Gdx.app.setLogLevel(LOG_LEVEL);
	}
	
	public static EntityManager initEngine(){
		if(engine != null){
			Gdx.app.error(TAG, "engine should be disposed before initalization");
			disposeEngine();
		}
		
		engine = new EntityManager();
		EntityFactory.setEngine(engine);
		EntityUtils.setEngine(engine);
		return engine;
	}
	
	public static void disposeEngine(){
		if(engine != null){
			engine.dispose();
		}
		engine = null;
	}
	
	public static void setScreen(AbstractScreen screen){
		if(game.getScreen() != null){
			game.getScreen().dispose();	
		}
		
		game.setScreen(screen);
	}
	
	/**
	 * @author Twiebs
	 * This shouldn't be used.  Anything that needs the engine should be in a system.
	 * It will be used for prototyping purposes for now.
	 * @return The games entity manager
	 */
	@Deprecated
	public static EntityManager getEngine(){
		return engine;
	}

	
	public static void exit(){
		disposeEngine();
		Gdx.app.exit();
	}

}
