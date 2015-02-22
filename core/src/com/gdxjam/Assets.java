package com.gdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
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

	
	public enum ArtStyle{
		PIXEL,
		MINIMAL_ALEX,
		MINIMAL_TWIEBS;
	}
	
	/**
	 * Developer flag used to test different art styles
	 */
	public static final ArtStyle style = ArtStyle.MINIMAL_ALEX;

	public static final String TEXTURE_ATLAS_OBJECTS = "assets.atlas";
	public static final String SKIN = "skin/uiskin.json";

	public AssetHotkey hotkey;
	public AssetFonts fonts;
	public AssetMinimal minimal;

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
			switch(style){
			default:
			case MINIMAL_ALEX:
				commander = atlas.findRegion("red");
				unit = atlas.findRegion("blue");
				tree = atlas.findRegion("green");
				break;
			case MINIMAL_TWIEBS:
				commander = atlas.findRegion("commander");
				unit = atlas.findRegion("unit");
				tree = atlas.findRegion("tree");
				break;
			}
			
			wall = atlas.createPatch("wall");
			wallRegion = atlas.findRegion("wall");
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
