package com.gdxjam.tests;

import junit.framework.TestCase;

import com.gdxjam.Main;
import com.gdxjam.screens.GameScreen;
import com.gdxjam.screens.MainMenuScreen;
import com.gdxjam.screens.OptionsScreen;
import com.gdxjam.screens.ScreenManager;

public class ScreenManagerTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		ScreenManager.instance.init(new Main());
	}

	public void testScreenManager() {		
		assertNotSame(ScreenManager.instance.getGameScreen(), new GameScreen());
		assertNotSame(ScreenManager.instance.getMainMenu(), new MainMenuScreen());
		assertNotSame(ScreenManager.instance.getOptionsScreen(), new OptionsScreen());
		
		assertEquals(ScreenManager.instance.getGameScreen(), ScreenManager.instance.getGameScreen());
		assertEquals(ScreenManager.instance.getMainMenu(), ScreenManager.instance.getMainMenu());
		assertEquals(ScreenManager.instance.getOptionsScreen(), ScreenManager.instance.getOptionsScreen());
		
		assertNotNull(ScreenManager.instance.getGameScreen());
		assertNotNull(ScreenManager.instance.getMainMenu());
		assertNotNull(ScreenManager.instance.getOptionsScreen());
	}
}
