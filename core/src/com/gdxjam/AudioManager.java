package com.gdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.gdxjam.OrionPrefs.BooleanValue;
import com.gdxjam.OrionPrefs.FloatValue;

public class AudioManager {
	
	private static final String MUSIC_DIR = "music/";
	private static final String SOUND_DIR = "sound/";
	
	public static boolean soundEnabled = true;
	public static boolean musicEnabled = true;
	
	public static float soundVolume = 1.0f;
	public static float musicVolume = 1.0f;
	
	public static Music currentMusic;
	
	public static void refresh(){
		soundEnabled = OrionPrefs.getBoolean(BooleanValue.AUDIO_SOUND_ENABLED);
		musicEnabled = OrionPrefs.getBoolean(BooleanValue.AUDIO_MUSIC_ENABLED);
		soundVolume = OrionPrefs.getFloat(FloatValue.AUDIO_SOUND_VOLUME);
		musicVolume = OrionPrefs.getFloat(FloatValue.AUDIO_MUSIC_VOLUME);
	}
	
	public static void playSound(Sound sound){
		if(soundEnabled){
			sound.play(soundVolume);
		}
	}
	
	public static void loadRandomTrack(){
		currentMusic = Gdx.audio.newMusic(Gdx.files.internal(MUSIC_DIR + Assets.music.gameTracks.random()));
		playMusic(currentMusic);
	}
	
	public static void playMusic(Music music){
		if(musicEnabled){
			music.setLooping(false);
			music.setVolume(musicVolume);
			music.play();
			music.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion (Music music) {
					music.dispose();
					loadRandomTrack();
				}
			});
		}
	}
	
	public static void stopMusic(){
		if(currentMusic != null){
			currentMusic.stop();
			currentMusic.dispose();
		}
	}

}
