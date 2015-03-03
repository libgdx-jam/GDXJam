package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ProjectileComponent extends Component implements Poolable{

	public int damage;
	
	public ProjectileComponent init(int damage){
		this.damage = damage;
		return this;
	}
	
	@Override
	public void reset () {
		
	}

}
