package com.gdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {

	public static final String TAG = Assets.class.getSimpleName();

	public static boolean rebuildAtlas = false;
	public static boolean drawDebugOutline = false;

	public static AssetManager manager;

	public static AssetManager getManager() {
		if (manager == null) {
			manager = new AssetManager();
		}
		return manager;
	}

	public static final String TEXTURE_ATLAS_OBJECTS = "assets.atlas";
	public static final String SKIN = "skin/uiskin.json";

	public static AssetHotkey hotkey;
	public static AssetFonts fonts;
	public static AssetSpace space;

	public static AssetSpacecraft spacecraft;

	public static void load() {
		getManager(); // Insure the manager exists
		manager.load(TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		manager.load(SKIN, Skin.class);
	}

	public static void create() {
		TextureAtlas atlas = manager.get(TEXTURE_ATLAS_OBJECTS);

		hotkey = new AssetHotkey(atlas);
		fonts = new AssetFonts();
		space = new AssetSpace(atlas);
		spacecraft = new AssetSpacecraft(atlas);
	}

	@Override
	public void dispose() {
		manager.dispose();
	}

	public static class AssetSpacecraft {
		public final AtlasRegion outpost;
		public final AtlasRegion ship;

		// public final AtlasRegion ship;

		public AssetSpacecraft(TextureAtlas atlas) {
			outpost = atlas.findRegion("outpost");
			ship = atlas.findRegion("ship");
		}
	}

	public static class AssetFonts {

		public final BitmapFont small;
		public final BitmapFont medium;
		public final BitmapFont large;

		public AssetFonts() {
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
					Gdx.files.internal("fonts/emulogic.ttf"));
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();
			parameter.size = 12;
			small = generator.generateFont(parameter); // font size 12 pixels

			parameter.size = 16;
			medium = generator.generateFont(parameter);

			parameter.size = 32;
			large = generator.generateFont(parameter);

			generator.dispose(); // don't forget to dispose to avoid memory
									// leaks!
		}

	}

	public static class AssetSpace {
		public AtlasRegion space;
		public final AtlasRegion largePlanetGreen;
		public final AtlasRegion largePlanetRed;

		public final AtlasRegion asteroid;

		public final AtlasRegion planet1;
		public final AtlasRegion planet2;
		public final AtlasRegion planet3;
		public final AtlasRegion planet4;
		public final AtlasRegion planet5;
		public final AtlasRegion planet6;
		public final AtlasRegion planet7;
		public final AtlasRegion planet8;
		public final AtlasRegion planet9;
		public final AtlasRegion planet10;

		public AssetSpace(TextureAtlas atlas) {
			space = atlas.findRegion("space");
			largePlanetGreen = atlas.findRegion("largegreenplanet");
			largePlanetRed = atlas.findRegion("largeredplanet");
			asteroid = atlas.findRegion("asteroid");
			planet1 = atlas.findRegion("planet1");
			planet2 = atlas.findRegion("planet2");
			planet3 = atlas.findRegion("planet3");
			planet4 = atlas.findRegion("planet4");
			planet5 = atlas.findRegion("planet5");
			planet6 = atlas.findRegion("planet6");
			planet7 = atlas.findRegion("planet7");
			planet8 = atlas.findRegion("planet8");
			planet9 = atlas.findRegion("planet9");
			planet10 = atlas.findRegion("planet10");
		}
	}

	public static class AssetHotkey {
		public final AtlasRegion left;
		public final NinePatch button;
		public AtlasRegion middle;
		public AtlasRegion right;

		public AssetHotkey(TextureAtlas atlas) {
			left = atlas.findRegion("hotkeyleft");
			button = atlas.createPatch("hotkey");
			right = atlas.findRegion("hotkeyright");
			middle = atlas.findRegion("middlehotkey");

		}
	}

}