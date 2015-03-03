package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.gdxjam.utils.Location2;

public class TargetComponent extends Component{
	
	public Entity target;
	
	public TargetComponent init(Entity target){
		this.target = target;
		return this;
	}

}
