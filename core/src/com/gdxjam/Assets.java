package com.gdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getSimpleName();
	private static Assets instance;
	public static AssetManager manager;

	public static Assets getInstance() {
		if (instance == null) {
			instance = new Assets();
		}
		return instance;
	}

	public static AssetManager getManager() {
		return manager;
	}

	public static final String TEXTURE_ATLAS_OBJECTS = "assets.atlas";
	public static final String SKIN = "skin/uiskin.json";

	public AssetHotkey hotkey;
	public AssetFonts fonts;
	public AssetMinimal minimal;
	public AssetPlanet planet;

	public Assets() {
		manager = new AssetManager();
		manager.setErrorListener(this); // set asset manager error handler

		loadAssets();

		Gdx.app.debug(TAG, "# of assets loaded: "
				+ manager.getAssetNames().size);
		for (String a : manager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);

			TextureAtlas atlas = manager.get(TEXTURE_ATLAS_OBJECTS);

			hotkey = new AssetHotkey(atlas);
			fonts = new AssetFonts();
			minimal = new AssetMinimal(atlas);
			planet = new AssetPlanet(atlas);
		}
	}

	public void loadAssets() {
		manager.load(TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		manager.load(SKIN, Skin.class);
		// manager.load("minimal.pack", TextureAtlas.class);
		manager.finishLoading();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		String filename = asset.fileName;
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'",
				(Exception) throwable);

	}

	@Override
	public void dispose() {
		manager.dispose();
	}

	public class AssetMinimal {
		public final AtlasRegion red, blue, green;
		public final AtlasRegion commander;
		public final AtlasRegion unit;
		public final AtlasRegion tree;

		public AssetMinimal(TextureAtlas atlas) {
			red = atlas.findRegion("red");
			blue = atlas.findRegion("blue");
			green = atlas.findRegion("green");
			// These extra references are going to be used once I make real
			// assets for them but now you can change the textures used all in
			// one
			// place @aplace21
			commander = red; // = atlas.findRegion("commander");
			unit = blue; // = atlas.findRegion("unit");
			tree = green; // = atlas.findRegion("tree);"
		}
	}

	public class AssetFonts {

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

	public class AssetPlanet {
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

		public AssetPlanet(TextureAtlas atlas) {
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

	public class AssetHotkey {
		public final AtlasRegion left;
		public final NinePatch button;
		public AtlasRegion middle;
		public AtlasRegion right;

		public AssetHotkey(TextureAtlas atlas) {
			left = atlas.findRegion("hotkeyleft");
			button = new NinePatch(atlas.findRegion("hotkey"));
			right = atlas.findRegion("hotkeyright");
			middle = atlas.findRegion("middlehotkey");

		}
	}

}
