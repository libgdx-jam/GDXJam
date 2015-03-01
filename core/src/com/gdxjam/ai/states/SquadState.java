package com.gdxjam.ai.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Cohesion;
import com.badlogic.gdx.ai.steer.behaviors.Separation;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.ai.steer.limiters.LinearLimiter;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.Components;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.UnitComponent;

public enum SquadState implements State<Entity>{
	
	MOVE(){
		@Override
		public void enter (Entity entity) {
//			SteeringBehaviorComponent behaviorComp = Components.STEERING_BEHAVIOR.get(entity);
//			SteerableComponent steerable = Components.STEERABLE.get(entity);
//
//			Arrive<Vector2> arriveSB = new Arrive<Vector2>(steerable)
//				.setLimiter(new LinearLimiter(300, 8)) //
//				.setTimeToTarget(0.1f) //
//				.setArrivalTolerance(0.001f) //
//				.setDecelerationRadius(2.0f);
//			
//			UnitComponent unit = Components.UNIT.get(entity);
//			
//			RadiusProximity<Vector2> cohesionProximity = new RadiusProximity<Vector2>(steerable, unit.squad.getAgents(), 8f);
//			RadiusProximity<Vector2> seperationProximity = new RadiusProximity<Vector2>(steerable, unit.squad.getAgents(), 2f);
//			
//			Cohesion<Vector2> cohesion = new Cohesion<Vector2>(steerable, cohesionProximity);
//			Separation<Vector2> separation = new Separation<Vector2>(steerable, seperationProximity);
//			Alignment<Vector2> align = new Alignment<Vector2>(steerable, cohesionProximity);
////			Arrive<Vector2> arrive = new Arrive<Vector2>(steerable, unit.squad.getTarget()).setTimeToTarget(0.01f).setArrivalTolerance(0.002f).setDecelerationRadius(5f);
////			
////			BlendedSteering<Vector2> behavior = new BlendedSteering<Vector2>(steerable);
////			behavior.add(separation, 1000);
////			behavior.add(cohesion, 0.15f);
////			behavior.add(align, 20f);
////			behavior.add(arrive, 4f);
////		
////			behaviorComp.setBehavior(behavior);
////			
//			Wander<Vector2> wanderSB = new Wander<Vector2>(steerable) //
//				.setLimiter(new LinearAccelerationLimiter(40)) //
//				.setFaceEnabled(false) // set to 0 because independent facing is off
//				.setAlignTolerance(0.001f) //
//				.setDecelerationRadius(5) //
//				.setTimeToTarget(0.1f) //
//				.setWanderOffset(1) //
//				.setWanderOrientation(10) //
//				.setWanderRadius(5) //
//				.setWanderRate(MathUtils.PI / 5);
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
