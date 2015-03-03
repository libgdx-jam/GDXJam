
package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;

public class SquadSystem extends IteratingSystem {

	private static final String TAG = "[" + SquadSystem.class.getSimpleName() + "]";

	public SquadSystem (GUISystem guiSystem) {
		super(Family.all(SquadComponent.class).get());
	}
	

	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
	}

	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
	}


	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		SquadComponent squadComp = Components.SQUAD.get(entity);
		squadComp.formation.updateSlots();
	}

}
