
package com.gdxjam;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.gdxjam.OrionPrefs.BooleanValue;
import com.gdxjam.OrionPrefs.StringValue;
import com.gdxjam.ecs.EntityManager;
import com.gdxjam.screens.AbstractScreen;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.EntityUtils;
import com.gdxjam.utils.WorldSpawner;

public class GameManager {
	private static final String TAG = "[" + GameManager.class.getSimpleName() + "]";

	private static Game game;
	private static EntityManager engine;
	
	private static boolean paused = false;

	public static void init (Game game) {
		GameManager.game = game;
		AudioManager.refresh();
		refreshDisplayMode();
		
		Gdx.app.setLogLevel(GameConfig.LOG_LEVEL);
	}

	public static EntityManager initEngine () {
		if (engine != null) {
			Gdx.app.error(TAG, "engine should be disposed before initalization");
			disposeEngine();
		}

		engine = new EntityManager();
		EntityFactory.setEngine(engine);
		EntityUtils.setEngine(engine);
		WorldSpawner.init(engine);
		return engine;
	}
	
	public static void refreshDisplayMode(){
		boolean fullscreen = OrionPrefs.getBoolean(BooleanValue.GRAPHICS_FULLSCREEN);
		String resolution = OrionPrefs.getString(StringValue.GRAPHICS_RESOLUTION);
		int width = Integer.valueOf(resolution.split("x")[0]);
		int height = Integer.valueOf(resolution.split("x")[1]);
		Gdx.graphics.setDisplayMode(width, height, fullscreen);
		
		if(game.getScreen() != null)
			game.getScreen().resize(width, height);
	}

	public static void disposeEngine () {
		if (engine != null) {
			engine.dispose();
		}
		engine = null;
	}

	public static void setScreen (AbstractScreen screen) {
		if (game.getScreen() != null) {
			game.getScreen().dispose();
		}

		game.setScreen(screen);
	}
	
	public static boolean isPaused(){
		return paused;
	}
	
	public static void pause(){
		paused = true;
	}
	public static void resume(){
		paused = false;
	}

	/** This shouldn't be used. Anything that needs the engine should be in a system. It will be used for prototyping purposes for
	 * now.
	 * @return The games entity manager */
	@Deprecated
	public static EntityManager getEngine () {
		return engine;
	}

	public static void exit () {
		disposeEngine();
		game.getScreen().dispose();
		Gdx.app.exit();
	}

	public static class GameConfig {
		public static enum BUILD {
			DEV, RELEASE;
		}

		public static String[] SUPPORTED_RESOLUTIONS = {"1280x720", "1920x1080",};

		public static final BUILD build = BUILD.DEV;
		private static final int LOG_LEVEL = Application.LOG_DEBUG;
	}

}
