package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.ecs.Components;

public class StateSystem extends IteratingSystem {

    public StateSystem() {
        super(Family.all(FSMComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
   	 Components.FSM.get(entity).update();
    }
}
