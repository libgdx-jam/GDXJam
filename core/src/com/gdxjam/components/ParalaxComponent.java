
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ParalaxComponent extends Component implements Poolable {

	public float worldX;
	public float worldY;
	public float worldWidth;
	public float worldHeight;
	public int layer;

	/** Can only be created by PooledEngine */
	private ParalaxComponent () {
		// private constructor
	}

	public ParalaxComponent init (float x, float y, float width, float height, int layer) {
		this.worldX = x;
		this.worldY = y;
		this.worldWidth = width;
		this.worldHeight = height;
		this.layer = layer;
		return this;
	}

	@Override
	public void reset () {
		// TODO Auto-generated method stub

	}
}
