package com.gdxjam.utils;

import com.gdxjam.components.FactionComponent.Faction;

public class Constants {

	public static enum BUILD {
		DEV, RELEASE;
	}

	public static final BUILD build = BUILD.DEV;

	public static boolean isPaused = false;

	public static final int maxSquads = 5;
	public static final int maxSquadMembers = 20;

	public static final boolean friendlyFire = false;

	public static final float unitRadius = 0.5f;
	public static final float mothershipRadius = 4f;
	public static final float projectileRadius = 0.25f;

	public static final float PIXEL_PER_METER = 32;

	public static final CharSequence GAME_TITLE = "Title Goes Here";

	// Supports the 16:9 ratio
	public static final float VIEWPORT_WIDTH = 64;
	public static final float VIEWPORT_HEIGHT = 36;

	public static Faction playerFaction = Faction.FACTION1;
	public static Faction enemyFaction = Faction.FACTION0;

	public static final String FACTION0_NAME = "Republic";
	public static final String FACTION1_NAME = "Alien";
	public static final String FACTION2_NAME = "Industrialist";

	public static final String FACTION0_DESC = FACTION0_NAME
			+ "\nThis is a test so you know";
	public static final String FACTION1_DESC = FACTION1_NAME
			+ "\nSo just ignore this for now";
	public static final String FACTION2_DESC = FACTION2_NAME
			+ "\nBut I think its working so you know.";

}
