
package com.gdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
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
	public static AssetsUI ui;
	public static AssetSpacecraft spacecraft;
	public static AssetProjectile projectile;
	public static AssetMusic music;
	public static AssetSound sound;
	
	public static Skin skin;

	public static void load () {
		getManager(); // Insure the manager exists
		manager.load(TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		manager.load(SKIN, Skin.class);
		loadSounds();
	}

	public static void loadParticles () {
		manager.load("particles/explosion.p", ParticleEffect.class);
	}
	
	public static void loadSounds(){
		manager.load("sound/boom.ogg", Sound.class);
	}

	public static void create () {
		TextureAtlas atlas = manager.get(TEXTURE_ATLAS_OBJECTS);
		
		skin = manager.get(SKIN);
		projectile = new AssetProjectile(atlas);
		hotkey = new AssetHotkey(atlas);
		fonts = new AssetFonts();
		space = new AssetSpace(atlas);
		spacecraft = new AssetSpacecraft(atlas);
		ui = new AssetsUI(atlas);
		music = new AssetMusic();
		sound = new AssetSound();
	}

	@Override
	public void dispose () {
		manager.dispose();
	}

	public static class AssetSpacecraft {
		public final Array<AtlasRegion> motherships;
		public final Array<AtlasRegion> ships;

		public AssetSpacecraft (TextureAtlas atlas) {
			motherships = atlas.findRegions("mothership");
			ships = atlas.findRegions("ship");
		}
	}

	public static class AssetFonts {

		public final BitmapFont font;

		public AssetFonts () {
			font = new BitmapFont(Gdx.files.internal("fonts/space.fnt"));
		}

	}

	public static class AssetsUI {
		public final Array<AtlasRegion> formationIcons;

		public AssetsUI (TextureAtlas atlas) {
			this.formationIcons = atlas.findRegions("formation");
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

	public static class AssetProjectile {
		public final Array<AtlasRegion> projectiles;

		public AssetProjectile (TextureAtlas atlas) {
			projectiles = atlas.findRegions("projectile");
		}
	}

	public static class AssetMusic {
		public static final Array<String> menuTracks = new Array<String>();
		public static final Array<String> gameTracks = new Array<String>();

		public AssetMusic () {
			menuTracks.addAll("menu.mp3");
			gameTracks.addAll("stars.mp3");
		}
	}
	
	public static class AssetSound{
		public final Sound boom = manager.get("sound/boom.ogg", Sound.class);
	}

}
