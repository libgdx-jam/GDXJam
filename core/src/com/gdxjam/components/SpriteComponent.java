
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;

public class SpriteComponent extends Component implements Poolable{
	
	public Sprite sprite;
	
	public SpriteComponent init(TextureRegion region, float x, float y, float width, float height){
		sprite = new Sprite(region);
		sprite.setBounds(x, y, width, height);
		sprite.setOriginCenter();
		return this;
	}
	
	public SpriteComponent init(Sprite sprite) {
		this.sprite = sprite;
		return this;
	}

	@Override
	public void reset () {
		sprite = null;
	}

}
