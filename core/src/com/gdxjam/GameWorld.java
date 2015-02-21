package com.gdxjam;

import com.gdxjam.utils.Constants;

public class GameWorld {

	public int width;
	public int height;
	
	public float time = 5 * Constants.secondsPerHour;
	public int food = 0;
	
	public GameWorld(int width, int height){
		this.width = width;
		this.height = height;
	}

}
