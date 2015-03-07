package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.gdxjam.utils.Range;

public class ResourceComponent extends Component implements Poolable{

	public Range capactiy = new Range(0, 1500);
	public int value;
	
	public ResourceComponent init(int amount) {
		capactiy = new Range(0, amount);
		this.value = amount;
		return this;
	}

	@Override
	public void reset () {
		value = 0;
	} 
    
 
}
