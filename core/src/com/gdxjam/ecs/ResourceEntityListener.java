
package com.gdxjam.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.utils.EntityUtils;

public class ResourceEntityListener implements EntityListener {

	private PooledEngine engine;

	public ResourceEntityListener (PooledEngine engine) {
		this.engine = engine;
	}

	@Override
	public void entityAdded (Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entityRemoved (Entity entity) {
		clearTarget(entity);
		EntityUtils.clearTarget(entity);
	}

	public void clearTarget (Entity target) {
		ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(TargetComponent.class, SquadComponent.class).get());

		for (Entity squad : entities) {
			SquadComponent squadComp = Components.SQUAD.get(squad);
			squadComp.untrack(squad, target); // The squad no longer will track the target
		}

	}
}
