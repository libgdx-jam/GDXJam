package com.gdxjam.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Cohesion;
import com.badlogic.gdx.ai.steer.behaviors.Separation;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.components.Components;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;

public class Squad {
	
	public SteerableTarget target;
	public Array<Entity> members;
	public Array<SteerableBodyComponent> agents;
	
	public Squad(Vector2 position){
		members = new Array<Entity>();
		agents = new Array<SteerableBodyComponent>();
		target = new SteerableTarget(position, 0.5f);
	}
	
	public void addMember(Entity entity){
		SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(entity);
		SteeringBehaviorComponent behaviorComponent = Components.STEERING_BEHAVIOR.get(entity);
		members.add(entity);
		agents.add(steerable);
		
		RadiusProximity<Vector2> cohesionProximity = new RadiusProximity<Vector2>(steerable, agents, 8f);
		RadiusProximity<Vector2> seperationProximity = new RadiusProximity<Vector2>(steerable, agents, 2f);
		
		Cohesion<Vector2> cohesion = new Cohesion<Vector2>(steerable, cohesionProximity);
		Separation<Vector2> separation = new Separation<Vector2>(steerable, seperationProximity);
		Alignment<Vector2> align = new Alignment<Vector2>(steerable, cohesionProximity);
		Arrive<Vector2> arrive = new Arrive<Vector2>(steerable, target).setTimeToTarget(0.01f).setArrivalTolerance(0.002f).setDecelerationRadius(5f);
		
		BlendedSteering<Vector2> behavior = new BlendedSteering<Vector2>(steerable);
		behavior.add(separation, 100);
		behavior.add(cohesion, 0.15f);
		behavior.add(align, 20f);
		behavior.add(arrive, 1f);
	
		behaviorComponent.setBehavior(behavior);
	}
	
	public void setTarget(Vector2 target){
		this.target.setPosition(target.x, target.y);
	}
	
	public void setTarget(float x, float y){
		target.setPosition(x, y);
	}
}
