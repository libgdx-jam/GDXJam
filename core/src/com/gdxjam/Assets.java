
package com.gdxjam;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {

	public static final String TAG = Assets.class.getSimpleName();

	public static boolean rebuildAtlas = false;
	public static boolean drawDebugOutline = false;

	public static AssetManager manager;

	public static AssetManager getManager () {
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
	public static Skin skin;

	public static void load () {
		getManager(); // Insure the manager exists
		manager.load(TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		manager.load(SKIN, Skin.class);
	}

	public static void create () {
		TextureAtlas atlas = manager.get(TEXTURE_ATLAS_OBJECTS);
		skin = manager.get(SKIN);
		hotkey = new AssetHotkey(atlas);
		fonts = new AssetFonts();
		space = new AssetSpace(atlas);
		spacecraft = new AssetSpacecraft(atlas);
	}

	@Override
	public void dispose () {
		manager.dispose();
	}

	public static class AssetSpacecraft {
		public final AtlasRegion outpost;
		public final AtlasRegion ship;
		public final AtlasRegion enemy;

		// public final AtlasRegion ship;

		public AssetSpacecraft (TextureAtlas atlas) {
			outpost = atlas.findRegion("outpost");
			ship = atlas.findRegion("ship");
			enemy = atlas.findRegion("enemy");
		}
	}

	public static class AssetFonts {

		public final BitmapFont small;
		public final BitmapFont medium;
		public final BitmapFont large;

		public AssetFonts () {
			small = skin.getFont("default-font");
			medium = skin.getFont("default-font");
			large = skin.getFont("default-font");
		}

	}

	public static class AssetSpace {
		public AtlasRegion background;

		public final Array<AtlasRegion> planets;
		public final Array<AtlasRegion> asteroids;

		public AssetSpace (TextureAtlas atlas) {
			background = atlas.findRegion("space");

			planets = atlas.findRegions("planet");
			asteroids = atlas.findRegions("asteroid");
		}
	}

	public static class AssetHotkey {
		public final AtlasRegion left;
		public final NinePatch button;
		public AtlasRegion middle;
		public AtlasRegion right;

		public AssetHotkey (TextureAtlas atlas) {
			left = atlas.findRegion("hotkeyleft");
			button = atlas.createPatch("hotkey");
			right = atlas.findRegion("hotkeyright");
			middle = atlas.findRegion("middlehotkey");

		}
	}

}
