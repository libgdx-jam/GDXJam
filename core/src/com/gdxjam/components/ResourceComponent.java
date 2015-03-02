package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ResourceComponent extends Component implements Poolable{

    public int amount;

	@Override
	public void reset () {
		amount = 0;
	} 
    
 
}
