package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.ai.SteerableTarget;
import com.gdxjam.components.Components;
import com.gdxjam.components.StateComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;

public class StateSystem extends IteratingSystem{
	
	private static final String TAG = "[" + StateSystem.class.getSimpleName() + "]"; 
	

	public StateSystem () {
		super(Family.all(StateComponent.class).get());
	}

	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		StateComponent state = Components.STATE.get(entity);
		state.stateMachine.update();
	}
	
	
}
