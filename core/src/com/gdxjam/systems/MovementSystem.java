package com.gdxjam.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.gdxjam.components.*;

/**
 * Created by SCAW on 15/02/2015.
 */
public class MovementSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<OrientationComponent> om = ComponentMapper.getFor(OrientationComponent.class);
    private ComponentMapper<AngularVelocityComponent> avm = ComponentMapper.getFor(AngularVelocityComponent.class);
    private ComponentMapper<ForceComponent> fm = ComponentMapper.getFor(ForceComponent.class);
    private ComponentMapper<TorqueComponent> tm = ComponentMapper.getFor(TorqueComponent.class);


    public void addedToEngine(Engine engine) {
        //at least it should have Position and Velocity
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }


    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);

            //apply Force
            if(fm.has(entity)){
                vm.get(entity).velocity.add(fm.get(entity).force.cpy().scl(deltaTime));
                //force reset every iteration
                fm.get(entity).force.set(0,0);
            }

            //apply linear motion: movement position = velocity * deltatime
            PositionComponent positionComponet = pm.get(entity);
            VelocityComponent velocityComponent = vm.get(entity);
            positionComponet.pos.add(velocityComponent.velocity.cpy().scl(deltaTime));


            //apply Torque on Angular Velocity
            if(tm.has(entity) && avm.has(entity)){
                avm.get(entity).angularVelocity += tm.get(entity).torque *deltaTime;
                //Torque reset every iteration
                tm.get(entity).torque = 0;
            }

            //apply rotation
            if(om.has(entity) && avm.has(entity)){
                om.get(entity).setHeading(om.get(entity).getHeading().rotateRad(avm.get(entity).angularVelocity * deltaTime));
            }

            System.out.println(vm.get(entity).velocity);

        }
    }
}
