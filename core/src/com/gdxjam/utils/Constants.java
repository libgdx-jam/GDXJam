package com.gdxjam.utils;

import com.badlogic.gdx.Input.Keys;

public class Constants {

	public static boolean pausedGUI = false;

	public static final int HOTKEY_PLATOON1 = Keys.NUM_1;
	public static final int HOTKEY_PLATOON2 = Keys.NUM_2;
	public static final int HOTKEY_PLATOON3 = Keys.NUM_3;
	public static final int HOTKEY_PLATOON4 = Keys.NUM_4;

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
