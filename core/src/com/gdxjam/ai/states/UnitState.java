package com.gdxjam.ai.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Cohesion;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Separation;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.EntityManager;
import com.gdxjam.ai.Messages;
import com.gdxjam.ai.SteerableTarget;
import com.gdxjam.components.CommanderHolderComponent;
import com.gdxjam.components.Components;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.ProximityComponent;
import com.gdxjam.components.RemovalComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.UnitComponent;


/**
 * Created by SCAW on 16/02/2015.
 */
public enum UnitState implements State<Entity> {
	
	IDLE(){
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(entity);
			SteeringBehaviorComponent behaviorComp = Components.STEERING_BEHAVIOR.get(entity);
			SteerableTarget target = new SteerableTarget(steerable.body.getPosition().cpy(), 0.5f);
			Seek<Vector2> seek = new Seek<Vector2>(steerable);
			seek.setTarget(target);
			
			Wander<Vector2> wander = new Wander<Vector2>(steerable);
			wander.setTarget(target).setWanderRate(5.0f).setWanderRadius(10.0f);
			
			BlendedSteering<Vector2> behavior = new BlendedSteering<Vector2>(steerable);
			behavior.add(seek, 1.0f);
			behavior.add(wander, 3.0f);
			
			behaviorComp.setBehavior(behavior);
		}
	},

    REGROUP(){
        @Override
        public void enter(Entity entity) {
            SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(entity);
            SteeringBehaviorComponent behaviorComponent = Components.STEERING_BEHAVIOR.get(entity);

            Proximity<Vector2> proximity = entity.getComponent(ProximityComponent.class).proximity;
            Cohesion<Vector2> cohesion = new Cohesion<Vector2>(steerable, proximity);
            Separation<Vector2> separation = new Separation<Vector2>(steerable, proximity);
            Alignment<Vector2> align = new Alignment<Vector2>(steerable, proximity);
            Arrive<Vector2> arrive = new Arrive<Vector2>(steerable, Components.STEERABLE_BODY.get(entity.getComponent(CommanderHolderComponent.class).general)).setTimeToTarget(0.01f).setArrivalTolerance(0.002f).setDecelerationRadius(1f);

            BlendedSteering<Vector2> behavior = new BlendedSteering<Vector2>(steerable);
            behavior.add(separation, 100);
            behavior.add(cohesion, 0.15f);
            behavior.add(align, 20f);
            behavior.add(arrive, 1f);

            behaviorComponent.setBehavior(behavior);
        }

        @Override
        public void exit(Entity entity) {
            SteeringBehaviorComponent behaviorComponent = Components.STEERING_BEHAVIOR.get(entity);
            behaviorComponent.getBehavior().setEnabled(false);
        }
    },

    FIGHT(){
        //TODO Behaviout tree
    },

    COLLECT_RESOURCES(){
       //TODO Behaviour Tree
   	 
   	 /**
   	  * Gather resource test
   	  */
   	 @Override
   	 public void enter(Entity entity){
      	 //Test code
      	 UnitComponent unit = Components.UNIT.get(entity);
   		 ImmutableArray<Entity> entities = EntityManager.getInstance().getEntitiesFor(Family.all(unit.assignedResource.component).get());
    		 Entity resource = entities.first();
    		 unit.target = resource;
    		 
    		 Arrive<Vector2> arrive = new Arrive<Vector2>(Components.STEERABLE_BODY.get(entity));
    		 arrive.setTimeToTarget(0.01f).setArrivalTolerance(0.002f).setDecelerationRadius(1f);
    		 arrive.setTarget(new SteerableTarget(Components.PHYSICS.get(resource).body.getPosition(), 1.0f));
    		 
    		 SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);
    		 behavior.setBehavior(arrive);
   	 }
   	 
   	 @SuppressWarnings("unchecked")
		public void update(Entity entity){
   		 UnitComponent unit = Components.UNIT.get(entity);
   		 
   		 if(unit.target == null){
   			 Components.STATE_MACHINE.get(entity).stateMachine.changeState(IDLE);
   		 }
   		 else{
      		 SteerableBodyComponent physics = Components.STEERABLE_BODY.get(entity);
      		 SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);

      		 Arrive arrive = (Arrive) behavior.getBehavior();
      		 Vector2 distance = physics.body.getPosition();
      		 distance.sub((Vector2) arrive.getTarget().getPosition());
      		 
      		 if(distance.len2() < 1.5f){
      			 HealthComponent health = Components.HEALTH.get(unit.target);
      			 health.value -= 1;
      			 if(health.value <= 0){
      				 unit.target = null;
      				 
      			 }
   		 }
   		 

   			 
   		 }
   		 
   		 
   	 }
	
    };



    @Override
    public void enter(Entity entity) {

    }

    @Override
    public void update(Entity entity) {

    }


    @Override
    public void exit(Entity entity) {

    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        switch (telegram.message){
            case Messages.REGROUP_ORDER:
                if(!Components.STATE_MACHINE.get(entity).stateMachine.isInState(REGROUP))
                    Components.STATE_MACHINE.get(entity).stateMachine.changeState(REGROUP);
                break;
            case Messages.RESOUCES_ORDER:
                Components.STATE_MACHINE.get(entity).stateMachine.changeState(COLLECT_RESOURCES);
        }

        return false;
    }
}
