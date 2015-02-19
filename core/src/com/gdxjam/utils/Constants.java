package com.gdxjam.utils;

public class Constants {

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

	public static boolean pausedGUI = false;

}
