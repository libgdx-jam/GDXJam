
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteComponent extends Component{
	
	public Sprite sprite;
	
	public SpriteComponent init(TextureRegion region, float x, float y, float width, float height){
		sprite = new Sprite(region);
		sprite.setBounds(x, y, width, height);
		sprite.setOriginCenter();
		return this;
	}

}
