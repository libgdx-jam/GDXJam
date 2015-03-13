
package com.gdxjam.utils;

import com.badlogic.gdx.math.MathUtils;

/** An inclusive range class
 * @author Torin Wiebelt */

public class Range {

	private float min;
	private float max;

	public Range (float min, float max) {
		this.min = min;
		this.max = max;
	}

	/** @return The minimum value of the range */
	public float min () {
		return min;
	}

	/** @return The maximum value of the range */
	public float max () {
		return max;
	}

	public float clamp (float value) {
		return MathUtils.clamp(value, min, max);
	}

	/** @param value the value to check
	 * @return true if the range contains the value (inclusive) */
	public boolean contains (float value) {
		return (value >= min && value <= max);
	}

	/** @return a random number within the range */
	public float random () {
		return MathUtils.random(min, max);
	}

	/** @param percent A number from [0.0, 1.0]
	 * @return the percent complete */
	public float percent (float percent) {
		return min + (max - min) * percent;
	}

}
