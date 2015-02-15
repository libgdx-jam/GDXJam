package com.gdxjam.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.gdxjam.components.PositionComponent;
import com.gdxjam.components.SteerableComponent;

/**
 * Created by SCAW on 15/02/2015.
 */
public class SteerableSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<SteerableComponent> scm = ComponentMapper.getFor(SteerableComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SteerableComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);


            SteerableComponent steerableComponent = e.getComponent(SteerableComponent.class);
            if(steerableComponent.getSteeringBehavior() == null) continue;

            steerableComponent.getSteeringBehavior().calculateSteering(steerableComponent.getSteeringOutput());
            //update force
            steerableComponent.getForceComponent().force.add(steerableComponent.getSteeringOutput().linear.cpy());
            //update torque
            steerableComponent.getTorqueComponent().torque += steerableComponent.getSteeringOutput().angular;



            //TODO move the following to movement system, or reate a linear drag force that opposes the velocity
            steerableComponent.getVelocityComponent().velocity.limit(steerableComponent.getMaxLinearSpeed());
            steerableComponent.getAngularVelocityComponent().angularVelocity = Math.min(steerableComponent.getMaxAngularSpeed(),steerableComponent.getAngularVelocity());

        }

    }
}
