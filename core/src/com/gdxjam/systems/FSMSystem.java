package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdxjam.GameManager;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.ecs.Components;

public class FSMSystem extends IteratingSystem {

    @SuppressWarnings("unchecked")
	public FSMSystem() {
        super(Family.all(FSMComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
   	 //Updates the entities finite state machine
   	 Components.FSM.get(entity).update();
    }
    
 	@Override
 	public boolean checkProcessing () {
 		return !GameManager.isPaused();
 	}
}
