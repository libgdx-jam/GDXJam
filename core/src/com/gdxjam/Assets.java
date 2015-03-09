package com.gdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
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

	public static AssetManager getManager() {
		if (manager == null) {
			manager = new AssetManager();
		}
		return manager;
	}

	public static final String TEXTURE_ATLAS_OBJECTS = "assets.atlas";
	public static final String SKIN = "skin/uiskin.json";

	public static MusicManager music;

	public static AssetHotkey hotkey;
	public static AssetFonts fonts;
	public static AssetSpace space;
	public static AssetsUI ui;
	public static AssetSpacecraft spacecraft;
	public static AssetProjectile projectile;
	public static AssetParticles particles;
	public static Skin skin;

	public static void load() {
		getManager(); // Insure the manager exists
		manager.load(TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		manager.load(SKIN, Skin.class);
	}

	public static void create() {
		TextureAtlas atlas = manager.get(TEXTURE_ATLAS_OBJECTS);
		skin = manager.get(SKIN);
		projectile = new AssetProjectile(atlas);
		hotkey = new AssetHotkey(atlas);
		fonts = new AssetFonts();
		space = new AssetSpace(atlas);
		spacecraft = new AssetSpacecraft(atlas);
		particles = new AssetParticles();
		ui = new AssetsUI(atlas);
		
		music = new MusicManager();
	}

	@Override
	public void dispose() {
		manager.dispose();
	}

	public static class AssetSpacecraft {
		public final Array<AtlasRegion> motherships;
		public final Array<AtlasRegion> ships;

		public AssetSpacecraft(TextureAtlas atlas) {
			motherships = atlas.findRegions("mothership");
			ships = atlas.findRegions("ship");
		}
	}

	public static class AssetParticles {
		ParticleEffectPool effectPool;
		public Array<ParticleEffect> effects;

		public AssetParticles() {
			effects = new Array<ParticleEffect>();
			ParticleEffect effect = new ParticleEffect();
			effect.load(Gdx.files.internal("particles/fire.p"),
					Gdx.files.internal(""));
			effectPool = new ParticleEffectPool(effect, 0, 99);
		}

		public PooledEffect getEffect() {
			PooledEffect effect = effectPool.obtain();
			effects.add(effect);
			return effect;
		}
	}

	public static class AssetFonts {

		public final BitmapFont font;

		public AssetFonts() {
			font = new BitmapFont(Gdx.files.internal("fonts/space.fnt"));
		}

	}
	
	public static class AssetsUI{
		public final Array<AtlasRegion> formationIcons;
		
		public AssetsUI(TextureAtlas atlas){
			this.formationIcons = atlas.findRegions("formation");
		}
	}

	public static class AssetSpace {
		public AtlasRegion background;

		public final Array<AtlasRegion> planets;
		public final Array<AtlasRegion> asteroids;

		public AssetSpace(TextureAtlas atlas) {
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

		public AssetHotkey(TextureAtlas atlas) {
			left = atlas.findRegion("hotkeyleft");
			button = atlas.createPatch("hotkey");
			right = atlas.findRegion("hotkeyright");
			middle = atlas.findRegion("middlehotkey");

		}
	}

	public static class AssetProjectile {
		public final Array<AtlasRegion> projectiles;

		public AssetProjectile(TextureAtlas atlas) {
			projectiles = atlas.findRegions("projectile");
		}
	}

	public static class MusicManager {
		private float volume = -1;
		private Preferences options;

		public Music music;

		Array<String> songs = new Array<String>(10);

		public MusicManager() {
			init();
		}

		public void init() {
			options = Gdx.app.getPreferences("Orion-options");
			songs.addAll("menu.mp3", "stars.mp3");
			// songs.addAll("shipadded.wav");
			music = Gdx.audio.newMusic(Gdx.files.internal("music/"
					+ songs.random()));
		}

		public float getVolume() {
			return volume > -1 ? volume : Gdx.app.getPreferences("options")
					.getFloat("volume", 20);
		}

		public void setVolume(float volume) {
			volume = MathUtils.clamp(volume, 0, 1);
			options.putFloat("volume", volume);
			options.flush();
		}

		public void play() {
			if (!music.isPlaying())
				music.play();
		}

		public void update() {
			if (!music.isPlaying()) {
				music = Gdx.audio.newMusic(Gdx.files.internal("music/"
						+ songs.random()));
				music.play();
			}
		}

		public void stop() {
			music.stop();
		}

		public void dispose() {
			music.dispose();
		}

		public void pause() {
			music.pause();
		}

	}
}
