package com.gdxjam.utils;

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
	public static final int maxSquads = 5;

	public static final float unitRadius = 0.5f;
	public static final float squadRadius = 1f;
	public static float mothershipRadius = 4f;

	public static final float PIXEL_PER_METER = 32;

	public static final CharSequence GAME_TITLE = "Title Goes Here";

	// Supports the 16:9 ratio
	public static final float VIEWPORT_WIDTH = 64;
	public static final float VIEWPORT_HEIGHT = 36;
}
