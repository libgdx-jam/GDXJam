package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.gdxjam.components.BehaviorTreeComponent;
import com.gdxjam.ecs.Components;

public class BehaviorTreeSystem extends IntervalIteratingSystem{

	public BehaviorTreeSystem () {
		super(Family.all(BehaviorTreeComponent.class).get(), PhysicsSystem.TIME_STEP / 2);
	}

	@Override
	protected void processEntity (Entity entity) {
		BehaviorTreeComponent btreeComp = Components.BTREE.get(entity);
		btreeComp.step();
	}
}
