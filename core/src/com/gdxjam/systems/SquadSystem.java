package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.Squad;

public class SquadSystem extends EntitySystem {

	private Array<Squad> squads = new Array<Squad>();

	public SquadSystem() {
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	public Array<Squad> getSquads() {
		return squads;
	}

	public void add(Squad squad) {
		squads.add(squad);
	}

}
