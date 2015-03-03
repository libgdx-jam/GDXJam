package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.gdxjam.utils.Location2;

public class SquadMemberComponent extends Component implements
		FormationMember<Vector2>, Poolable {

	public Entity squad;
	
	public Location2 targetLocation = new Location2();

	public SquadMemberComponent() {
		targetLocation = new Location2(new Vector2(0, 0));
	}
	
	public SquadMemberComponent init(Entity squad){
		this.squad = squad;
		return this;
	}

	@Override
	public Location<Vector2> getTargetLocation() {
		return targetLocation;
	}

	@Override
	public void reset() {
		targetLocation = new Location2();
	}
}
