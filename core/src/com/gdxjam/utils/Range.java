package com.gdxjam.utils;

import com.badlogic.gdx.math.MathUtils;

/**
 * 
 * @author Torin Wiebelt (twiebs)
 * Utilities class for ranges
 */

public class Range {
	
	public float min;
	public float max;
	
	public Range(float min, float max){
		this.min = min;
		this.max = max;
	}
	
	public float random(){
		return MathUtils.random(min, max);
	}
	/**
	 * 
	 * @param scl A scalar from [0.0, 1.0]
	 * @return a random number within the range.
	 */
	public float random(float scl){
		return min + ((max - min) * scl);
	}

}
