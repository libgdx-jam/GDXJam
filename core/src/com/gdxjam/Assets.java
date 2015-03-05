package com.gdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
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
}
