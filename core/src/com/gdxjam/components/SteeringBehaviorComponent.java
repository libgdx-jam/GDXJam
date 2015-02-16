package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;

public class SteeringBehaviorComponent extends Component {

	private SteeringBehavior<Vector2> behavior;
	
	public SteeringBehaviorComponent init(SteeringBehavior<Vector2> behavior){
		setBehavior(behavior);
		return this;
	}
	
	public void setBehavior(SteeringBehavior<Vector2> behavior){
		this.behavior = behavior;
	}
	
	public SteeringBehavior<Vector2> getBehavior(){
		return behavior;
	}

}
