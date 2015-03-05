package com.gdxjam.components;

import com.badlogic.ashley.core.Component;

public class DecayComponent extends Component{
	
	public float elapsed = 0;
	public float decayTime = 1;
	
	public DecayComponent init(float decayTime){
		this.decayTime = decayTime;
		return this;
	}

}
