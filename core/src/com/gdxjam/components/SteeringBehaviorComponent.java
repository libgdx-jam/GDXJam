package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SteeringBehaviorComponent extends Component {

	private SteeringBehavior<Vector2> behavior;
	
	public SteeringBehavior<Vector2> getBehavior(){
		return behavior;
	}

}
