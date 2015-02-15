package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent extends Component {
	public Vector2 pos;

	public PositionComponent(float x, float y) {
        this(new Vector2(x,y));
	}

    public PositionComponent(Vector2 pos){
        this.pos = pos;
    }
}
