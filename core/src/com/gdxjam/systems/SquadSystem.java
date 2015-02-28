package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FreeSlotAssignmentStrategy;
import com.badlogic.gdx.ai.fma.patterns.DefensiveCircleFormationPattern;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.limiters.LinearLimiter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.Squad;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.UnitComponent;
import com.gdxjam.utils.Constants;

public class SquadSystem extends EntitySystem {

	private static final String TAG = "[" + SquadSystem.class.getSimpleName()
			+ "]";
	public Array<Squad> squads;

	private int population;

	private GUISystem guiSystem;
	private PooledEngine engine;

	public SquadSystem(GUISystem guiSystem) {
		squads = new Array<Squad>(true, Constants.maxSquads);
		this.guiSystem = guiSystem;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.engine = (PooledEngine) engine;
	}

	public Squad createSquad(Vector2 position) {
		int index = squads.size;
		Squad squad = new Squad(position, index);
		DefensiveCircleFormationPattern<Vector2> pattern = new DefensiveCircleFormationPattern<Vector2>(
				Constants.unitRadius);
		FreeSlotAssignmentStrategy<Vector2> slotAssignmentStrategy = new FreeSlotAssignmentStrategy<Vector2>();

		squad.formation = new Formation<Vector2>(squad.anchor, pattern,
				slotAssignmentStrategy);

		squads.add(squad);
		guiSystem.addSquad(squad);
		return squad;
	}

	public void addMember(Entity entity, Squad squad) {
		SquadMemberComponent squadMember = engine
				.createComponent(SquadMemberComponent.class);
		entity.add(squadMember);

		SteerableComponent steer = Components.STEERABLE.get(entity);
		SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR
				.get(entity);

		Arrive<Vector2> arriveSB = new Arrive<Vector2>(steer,
				squadMember.getTargetLocation())
				.setLimiter(new LinearLimiter(3500, 10)).setTimeToTarget(0.1f)
				.setArrivalTolerance(0.001f).setDecelerationRadius(3f);
		behavior.setBehavior(arriveSB);

		squad.formation.addMember(squadMember);
	}

	public void setTarget(Vector2 target) {
		for (Squad squad : squads) {
			if (squad.isSelected()) {
				squad.setTarget(target);
			}
		}
	}

	public void setState(State<Entity> state) {
		for (Squad squad : squads) {
			if (squad.selected) {
				setState(state, squad);
			}
		}
	}

	public void setState(State<Entity> state, Squad squad) {
		squad.state = state;
		for (Entity entity : squad.entities) {
			Components.STATE_MACHINE.get(entity).stateMachine
					.changeState(state);
		}
	}

	public boolean toggleSelected(Squad squad) {
		squad.selected = !squad.selected;
		guiSystem.setSelected(squad);
		return squad.selected;
	}

	public boolean toggleSelected(int index) {
		if (squads.size > index) {
			return toggleSelected(squads.get(index));
		}
		return false;
	}

	public Squad addUnitToSquad(Entity entity, Squad squad) {

		if (Components.UNIT.has(entity)) {
			UnitComponent unit = Components.UNIT.get(entity);
			unit.squad = squad;

			squad.addEntity(entity);
			Components.STATE_MACHINE.get(entity).stateMachine
					.changeState(squad.state);
		} else {
			Gdx.app.error(TAG, "entity cannot be added to squad " + squad.index
					+ " : Entity is not a unit");
		}
		return squad;
	}

	public Array<Squad> getSquads() {
		return squads;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		for (Squad squad : squads) {
			squad.formation.updateSlots();
		}
	}

}
