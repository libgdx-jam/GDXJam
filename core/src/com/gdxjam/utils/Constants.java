package com.gdxjam.utils;

import com.badlogic.gdx.Input.Keys;

public class Constants {

	public static boolean pausedGUI = false;

	// Time
	public static final float secondsPerMinute = 0.1f;
	public static final float secondsPerHour = secondsPerMinute * 60;

	public static final float sunriseBegin = 6 * secondsPerHour;
	public static final float sunriseEnd = 7 * secondsPerHour;
	public static final float sunriseDuration = sunriseEnd - sunriseBegin;

	public static final float sunsetBegin = 18 * secondsPerHour;
	public static final float sunsetEnd = 19 * secondsPerHour;
	public static final float sunsetDuration = sunsetEnd - sunsetBegin;

	// Keybinds
	public static final int HOTKEY_PLATOON1 = Keys.NUM_1;
	public static final int HOTKEY_PLATOON2 = Keys.NUM_2;
	public static final int HOTKEY_PLATOON3 = Keys.NUM_3;
	public static final int HOTKEY_PLATOON4 = Keys.NUM_4;

	public static final String BLOCK_TYPE = "tiletype";

}
