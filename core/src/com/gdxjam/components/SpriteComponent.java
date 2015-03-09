
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;

public class SpriteComponent extends Component implements Poolable {

	private Sprite sprite;

	/** Can only be created by PooledEngine */
	private SpriteComponent () {
		// private constructor
	}

	public SpriteComponent init (TextureRegion region, float x, float y, float width, float height) {
		sprite = new Sprite(region);
		sprite.setBounds(x, y, width, height);
		sprite.setOriginCenter();
		return this;
	}

	public SpriteComponent init (Sprite sprite) {
		this.sprite = sprite;
		return this;
	}

	public Sprite getSprite () {
		return sprite;
	}

	@Override
	public void reset () {
		sprite = null;
	}

}
