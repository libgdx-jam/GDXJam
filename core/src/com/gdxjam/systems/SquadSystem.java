
package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.Squad;
import com.gdxjam.ai.formation.SquadFormationPattern;
import com.gdxjam.ai.states.UnitState;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.utils.Constants;

public class SquadSystem extends IteratingSystem {

	private static final String TAG = "[" + SquadSystem.class.getSimpleName() + "]";

	public Array<Squad> squads;
	private GUISystem guiSystem;
	private PooledEngine engine;

	public SquadSystem (GUISystem guiSystem) {
		super(Family.all(SquadComponent.class).get());
		
		squads = new Array<Squad>(true, Constants.maxSquads);
		this.guiSystem = guiSystem;
		
		SquadFormationPattern.initPatterns();
	}
	

	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		this.engine = (PooledEngine)engine;
	}

	public Squad createSquad (Vector2 position) {
		int index = squads.size;
		Squad squad = new Squad(position, index);
		
		squads.add(squad);
		guiSystem.addSquad(squad);
		return squad;
	}

	public void addMember (Entity entity, Squad squad) {
		SquadMemberComponent squadMember = engine.createComponent(SquadMemberComponent.class);
		entity.add(squadMember);
		Components.STATE_MACHINE.get(entity).stateMachine.changeState(UnitState.FORMATION);
		
		squad.addMember(entity);
		guiSystem.updateSquad(squad);
	}

	public void setTarget (Vector2 target) {
		for (Squad squad : squads) {
			if (squad.isSelected()) {
				squad.setTarget(target);
			}
		}
	}

	public boolean toggleSelected (Squad squad) {
		squad.selected = !squad.selected;
		guiSystem.setSelected(squad);
		return squad.selected;
	}

	public boolean toggleSelected (int index) {
		if (squads.size > index) {
			return toggleSelected(squads.get(index));
		}
		return false;
	}

	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
	}


	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		SquadComponent squadComp = Components.SQUAD.get(entity);
		squadComp.formation.updateSlots();
	}

}
