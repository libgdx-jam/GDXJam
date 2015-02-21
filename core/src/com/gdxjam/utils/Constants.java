package com.gdxjam.utils;

import com.badlogic.gdx.Input.Keys;

public class Constants {

	public static boolean pausedGUI = false;
	
	//Time
	public static final float secondsPerMinute = 0.1f;
	public static final float secondsPerHour = secondsPerMinute * 60;
	
	public static final float sunriseBegin = 6 * secondsPerHour;
	public static final float sunriseEnd = 7 * secondsPerHour;
	public static final float sunriseDuration = sunriseEnd - sunriseBegin;
	
	public static final float sunsetBegin = 18 * secondsPerHour;
	public static final float sunsetEnd = 19 * secondsPerHour;
	public static final float sunsetDuration = sunsetEnd - sunsetBegin;

	//Keybinds
	
	public static final int maxSquads = 5;
	
	
	public static final String BLOCK_TYPE = "tiletype";

	public enum BLOCK_TYPE {
		// (r,g,b) 0-255
		EMPTY(0, 0, 0), // black
		FLOOR(0, 255, 0), // green
		POST1(255, 255, 255), // white
		POST2(255, 255, 0); // yellow

		private int color;

		private BLOCK_TYPE(int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}

		public boolean sameColor(int color) {
			return this.color == color;
		}

		public int getColor() {
			return color;
		}
	}

}
