package com.gdxjam.utils;

import com.gdxjam.components.FactionComponent.Faction;

public class Constants {

	public static enum WorldSize {
		SMALL, MEDIUM, LARGE, CUSTOM;
	}

	public static final int maxSquads = 5;
	public static final int maxSquadMembers = 20;

	public static final boolean friendlyFire = false;
	public static final float resourceCollectionSpeed = 0.25f;

	public static final float unitRadius = 0.5f;
	public static final float mothershipRadius = 4f;
	public static final float projectileRadius = 0.25f;
	public static final float projectileDecayTime = 2.0f;
	public static final float baseAsteroidResourceAmt = 100;

	public static final float PIXEL_PER_METER = 32;

	public static final CharSequence GAME_TITLE = "Orion";

	// Supports the 16:9 ratio
	public static final float VIEWPORT_WIDTH = 64;
	public static final float VIEWPORT_HEIGHT = 36;

	public static WorldSize worldSize = WorldSize.MEDIUM;
	public static Faction playerFaction = Faction.FACTION1;
	public static Faction enemyFaction = Faction.FACTION0;

}
