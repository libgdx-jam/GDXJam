package com.gdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class OrionPrefs {
	
	private static Preferences prefs = Gdx.app.getPreferences("orion");
	
	public static enum StringValue{
		GRAPHICS_RESOLUTION("1280x720"),
		;
		
		private String defaultValue;
		private StringValue(String defaultValue){
			this.defaultValue = defaultValue;
		}
	}
	
	public static enum FloatValue{
		AUDIO_MUSIC_VOLUME(1.0f),
		AUDIO_SOUND_VOLUME(1.0F);
		
		public float defaultValue;
		private FloatValue(float defaultValue){
			this.defaultValue = defaultValue;
		}
	}
	
	public static enum BooleanValue{
		AUDIO_MUSIC_ENABLED(true),
		AUDIO_SOUND_ENABLED(true),
		
		GRAPHICS_FULLSCREEN(false),
		;
		
		private boolean defaultValue;
		private BooleanValue(boolean defaultValue){
			this.defaultValue = defaultValue;
		}
	}
	
	public static void putFloat(FloatValue key, float value){
		prefs.putFloat(key.toString(), value);
		prefs.flush();
	}
	
	public static float getFloat(FloatValue key){
		return prefs.getFloat(key.toString(), key.defaultValue);
	}
	
	public static String getString(StringValue key){
		return prefs.getString(key.toString(), key.defaultValue);
	}
	
	public static void putString(StringValue key, String value){
		prefs.putString(key.toString(), value);
		prefs.flush();
	}
	
	public static void putBoolean(BooleanValue key, boolean value){
		prefs.putBoolean(key.toString(), value);
	}
	
	public static boolean getBoolean(BooleanValue key){
		return prefs.getBoolean(key.toString(), key.defaultValue);
	}
	
	
}
