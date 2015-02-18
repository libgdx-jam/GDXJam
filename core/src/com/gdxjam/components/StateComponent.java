package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Cohesion;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Separation;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.ai.SteerableTarget;

public class StateComponent extends Component{
	
	public enum SquadState implements State<Entity>{
		IDLE(){
			@Override
			public void enter (Entity entity) {
				SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(entity);
				SteeringBehaviorComponent behaviorComponent = Components.STEERING_BEHAVIOR.get(entity);
				SquadMemberComponent member = Components.SQUAD_MEMBER.get(entity);
				
				RadiusProximity<Vector2> cohesionProximity = new RadiusProximity<Vector2>(steerable, member.squad.agents, 8f);
				RadiusProximity<Vector2> seperationProximity = new RadiusProximity<Vector2>(steerable, member.squad.agents, 2f);
				
				Cohesion<Vector2> cohesion = new Cohesion<Vector2>(steerable, cohesionProximity);
				Separation<Vector2> separation = new Separation<Vector2>(steerable, seperationProximity);
				Alignment<Vector2> align = new Alignment<Vector2>(steerable, cohesionProximity);
				Arrive<Vector2> arrive = new Arrive<Vector2>(steerable, member.squad.target).setTimeToTarget(0.01f).setArrivalTolerance(0.002f).setDecelerationRadius(5f);
				
				BlendedSteering<Vector2> behavior = new BlendedSteering<Vector2>(steerable);
				behavior.add(separation, 100);
				behavior.add(cohesion, 0.15f);
				behavior.add(align, 20f);
				behavior.add(arrive, 1f);
			
				behaviorComponent.setBehavior(behavior);
			}
		};

		@Override
		public void enter (Entity entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update (Entity entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void exit (Entity entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	public enum WorkerState implements State<Entity>{
		IDLE(){
			@Override
			public void enter (Entity entity) {
				super.enter(entity);
				SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(entity);
				SteeringBehaviorComponent behaviorComp = Components.STEERING_BEHAVIOR.get(entity);
				SteerableTarget target = new SteerableTarget(new Vector2(10, 10), 0.5f);
				Seek<Vector2> seek = new Seek<Vector2>(steerable);
				seek.setTarget(target);
				
				Wander<Vector2> wander = new Wander<Vector2>(steerable);
				wander.setTarget(target).setWanderRate(5.0f).setWanderRadius(1.0f);
				
				BlendedSteering<Vector2> behavior = new BlendedSteering<Vector2>(steerable);
				behavior.add(seek, 1.0f);
				behavior.add(wander, 2.0f);
				
				behaviorComp.setBehavior(behavior);
			}
		};

		@Override
		public void enter (Entity entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update (Entity entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void exit (Entity entity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	public StateMachine stateMachine;

	public StateComponent init(Entity entity){
		stateMachine = new DefaultStateMachine<Entity>(entity, WorkerState.IDLE);
		stateMachine.changeState(WorkerState.IDLE);
		return this;
	}

}
