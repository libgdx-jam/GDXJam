//
//package com.gdxjam.components;
//
//import com.badlogic.ashley.core.Component;
//import com.badlogic.ashley.core.Entity;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.Pool.Poolable;
//import com.gdxjam.ecs.Components;
//
//public class TargetFinderComponent extends Component implements Poolable {
//
//	public Array<Entity> resources = new Array<Entity>();
//	public Array<SteerableComponent> resourceAgents = new Array<SteerableComponent>();
//
//	public Array<Entity> squads = new Array<Entity>();
//
//	/** Can only be created by PooledEngine */
//	private TargetFinderComponent () {
//		// private constructor
//	}
//
//	public void resource (Entity entity, boolean remove) {
//		if (remove) {
//			resources.removeValue(entity, true);
//		} else {
//			if (!resources.contains(entity, true)) {
//				resources.add(entity);
//				resourceAgents.add(Components.STEERABLE.get(entity));
//			}
//		}
//	}
//
//	public void squad (Entity entity, boolean remove) {
//		if (remove) {
//			squads.removeValue(entity, true);
//		} else {
//			if (!squads.contains(entity, true)) squads.add(entity);
//		}
//	}
//
//	@Override
//	public void reset () {
//		resources.clear();
//		squads.clear();
//	}
//
//}
