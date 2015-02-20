package com.gdxjam.ai.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Cohesion;
import com.badlogic.gdx.ai.steer.behaviors.Separation;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;

public enum SquadState implements State<Entity>{
	MOVE(){
		@Override
		public void enter (Entity entity) {
			SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(entity);
			SteeringBehaviorComponent behaviorComponent = Components.STEERING_BEHAVIOR.get(entity);
			SquadMemberComponent member = Components.SQUAD_MEMBER.get(entity);
			
			RadiusProximity<Vector2> cohesionProximity = new RadiusProximity<Vector2>(steerable, member.squad.getAgents(), 8f);
			RadiusProximity<Vector2> seperationProximity = new RadiusProximity<Vector2>(steerable, member.squad.getAgents(), 2f);
			
			Cohesion<Vector2> cohesion = new Cohesion<Vector2>(steerable, cohesionProximity);
			Separation<Vector2> separation = new Separation<Vector2>(steerable, seperationProximity);
			Alignment<Vector2> align = new Alignment<Vector2>(steerable, cohesionProximity);
			Arrive<Vector2> arrive = new Arrive<Vector2>(steerable, member.squad.getTarget()).setTimeToTarget(0.01f).setArrivalTolerance(0.002f).setDecelerationRadius(5f);
			
			BlendedSteering<Vector2> behavior = new BlendedSteering<Vector2>(steerable);
			behavior.add(separation, 1000);
			behavior.add(cohesion, 0.15f);
			behavior.add(align, 20f);
			behavior.add(arrive, 4f);
		
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
