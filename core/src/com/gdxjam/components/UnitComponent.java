
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.gdxjam.utils.Location2;

public class UnitComponent extends Component implements FormationMember<Vector2>, Poolable {

	private Entity squad;
	private Body body;
	private Location2 targetLocation = new Location2();

	/** Can only be created by PooledEngine */
	private UnitComponent () {
		// private constructor
	}

	public UnitComponent init (Entity squad, Body body) {
		this.squad = squad;
		this.body = body;
		return this;
	}

	public Entity getSquad () {
		return squad;
	}

	public Body getBody () {
		return body;
	}

	@Override
	public Location<Vector2> getTargetLocation () {
		return targetLocation;
	}

	@Override
	public void reset () {

	}
}
