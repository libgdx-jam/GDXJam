package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TargetComponent extends Component implements Poolable{
	
	public Entity target;
	
	public TargetComponent init(Entity target){
		this.target = target;
		return this;
	}

	@Override
	public void reset () {
		target = null;
	}

}
