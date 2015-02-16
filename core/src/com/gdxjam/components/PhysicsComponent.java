package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class PhysicsComponent extends Component{
	
	public Body body;
	
	public PhysicsComponent init(Body body){
		this.body = body;
		return this;
	}
	
	public Body getBody(){
		return body;
	}
	
}
