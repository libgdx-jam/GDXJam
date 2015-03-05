package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class WeaponComponent extends Component implements Poolable{

	public float cooldown = 0.0f;
	public float attackSpeed = 1.0f;
	public float damage = 20;
	
	public WeaponComponent init(int damage, float attackSpeed){
		this.damage = damage;
		this.attackSpeed = attackSpeed;
		return this;
	}
	
	
	@Override
	public void reset () {
		cooldown = 0.0f;
	}

}
