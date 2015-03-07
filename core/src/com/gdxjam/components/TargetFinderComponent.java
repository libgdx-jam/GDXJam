package com.gdxjam.components;

import java.util.Comparator;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TargetFinderComponent extends Component implements Poolable {

	public Array<Entity> resources = new Array<Entity>();
	public Array<SteerableComponent> resourceAgents = new Array<SteerableComponent>(); 
	
	public Array<Entity> squads = new Array<Entity>();
	
	public TargetFinderComponent(){
		
	}
	
	public void sortResources(){
		resources.sort(new Comparator<Entity>() {
			@Override
			public int compare (Entity e1, Entity e2) {
				Vector2 pos1 = Components.PHYSICS.get(e1).body.getPosition();
				Vector2 pos2 = Components.PHYSICS.get(e2).body.getPosition();
			
				return 0;
			}
			
		});
	}

	public void resource(Entity entity, boolean remove) {
		if(remove){
			resources.removeValue(entity, true);
		}
		else{
			if (!resources.contains(entity, true)) {
				resources.add(entity);
				resourceAgents.add(Components.STEERABLE.get(entity));
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
