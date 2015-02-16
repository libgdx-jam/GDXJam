package com.gdxjam.screens;

import com.badlogic.gdx.Screen;
import com.gdxjam.Main;
import com.gdxjam.ai.test.TestScreen;

public class ScreenManager {

	Main main;

	private MainMenuScreen mainMenu;
	private GameScreen gameScreen;
	private OptionsScreen optionsScreen;

	public static final ScreenManager instance = new ScreenManager();

	private ScreenManager() {
	}

	public Screen getDefault() {
		return (Screen) new TestScreen();
	}

	public void init(Main main) {
		this.main = main;
		mainMenu = new MainMenuScreen();
		gameScreen = new GameScreen();
		optionsScreen = new OptionsScreen();
	}

	public void setScreen(Screen screen) {
		main.setScreen(screen);
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public MainMenuScreen getMainMenu() {
		return mainMenu;
	}

	public OptionsScreen getOptionsScreen() {
		return optionsScreen;
	}

}
