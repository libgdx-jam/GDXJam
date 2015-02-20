package com.gdxjam;

import com.gdxjam.map.GameMapPixMap;
import com.gdxjam.utils.Constants;

public class GameWorld {

	public int width;
	public int height;
	public GameMapPixMap map;
	
	public float time = 5 * Constants.secondsPerHour;
	public int food = 0;
	
	public GameWorld(int width, int height){
		this.width = width;
		this.height = height;
	}

}
