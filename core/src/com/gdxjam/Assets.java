package com.gdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getSimpleName();
	
	private static Assets instance;

	public static Assets getInstance() {
		if (instance == null) {
			instance = new Assets();
		}
		return instance;
	}


	public static AssetManager manager;

	public static AssetManager getManager() {
		return manager;
	}

	public static final String TEXTURE_ATLAS_OBJECTS = "assets.atlas";
	public static final String SKIN = "skin/uiskin.json";

	public static AssetHotkey hotkey;
	public static AssetFonts fonts;
	public static AssetMinimal minimal;
	public static AssetPlanet planet;
	public static AssetMothership mothership;
	public static AssetSpacecraft spacecraft;
	public static AssetSpace space;

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
			mothership = new AssetMothership(atlas);
			spacecraft = new AssetSpacecraft(atlas);
			space = new AssetSpace(atlas);
			
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
		public final AtlasRegion commander;
		public final AtlasRegion unit;
		public final AtlasRegion tree;
		public final NinePatch wall;
		public final AtlasRegion wallRegion;

		public AssetMinimal(TextureAtlas atlas) {

			commander = atlas.findRegion("commander");
			unit = atlas.findRegion("unit");
			tree = atlas.findRegion("tree");

			wall = atlas.createPatch("wall");
			wallRegion = atlas.findRegion("wall");
		}
	}

	public static class AssetSpacecraft {
		public final AtlasRegion outpost;

		// public final AtlasRegion ship;

		public AssetSpacecraft(TextureAtlas atlas) {
			outpost = atlas.findRegion("outpost");
		}
	}

	public class AssetMothership {
		public final AtlasRegion ship;

		public AssetMothership(TextureAtlas atlas) {
			ship = atlas.findRegion("att3");
		}

	}
	
	public class AssetSpace{
		public final AtlasRegion asteroid;
		
		public AssetSpace(TextureAtlas atlas){
			this.asteroid = atlas.findRegion("asteroid");
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
			button = atlas.createPatch("hotkey");
			right = atlas.findRegion("hotkeyright");
			middle = atlas.findRegion("middlehotkey");

		}
	}

}