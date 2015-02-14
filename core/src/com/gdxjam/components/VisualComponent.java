package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class VisualComponent extends Component {
	public TextureRegion region;
	public float rotation;

	public VisualComponent(TextureRegion region, float rotation) {
		this.region = region;
		this.rotation = rotation;
	}
}
