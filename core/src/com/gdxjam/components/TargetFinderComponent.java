package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TargetFinderComponent extends Component implements Poolable {

	public Array<Entity> resources = new Array<Entity>();
	public Array<Entity> squads = new Array<Entity>();

	public void resource(Entity entity, boolean remove) {
		if(remove){
			resources.removeValue(entity, true);
		}
		else{
			if (!resources.contains(entity, true)) {
				resources.add(entity);
			}
		}
	}

	public void squad(Entity entity, boolean remove) {
		if(remove){
			squads.removeValue(entity, true);
		}
		else{
			if(!squads.contains(entity, true))
				squads.add(entity);
		}
	}

	@Override
	public void reset() {
		resources.clear();
		squads.clear();
	}

}
