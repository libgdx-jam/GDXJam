
package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.ecs.Components;

public class SquadSystem extends IteratingSystem {

	private static final String TAG = "[" + SquadSystem.class.getSimpleName() + "]";
	private Array<Entity> selectedSquads;
	
	public SquadSystem (GUISystem guiSystem) {
		super(Family.all(SquadComponent.class).get());
		selectedSquads = new Array<Entity>();
	}
	
	public void setSelected(Entity squad, boolean selected){
		if(!selectedSquads.contains(squad, true))
			selectedSquads.add(squad);
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
