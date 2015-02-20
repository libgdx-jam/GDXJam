package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.gdxjam.components.Components;
import com.gdxjam.components.StateMachineComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;

/**
 * Created by SCAW on 16/02/2015.
 */
public class StateMachineSystem extends IteratingSystem {


    public StateMachineSystem() {
        super(Family.all(StateMachineComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {


        StateMachine stateMachine = Components.STATE_MACHINE.get(entity).stateMachine;
        stateMachine.update();
    }
}
