package com.gdxjam.ai.formation;

import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class LineFormationPattern implements FormationPattern<Vector2> {

	private int numberOfSlots;
	private float memberRadius;

	public LineFormationPattern(float memberRadius) {
		this.memberRadius = memberRadius;
	}

	@Override
	public void setNumberOfSlots(int numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
	}

	@Override
	public Location<Vector2> calculateSlotLocation(Location<Vector2> outLocation, int slotNumber) {
		float centerOffset = (numberOfSlots * (memberRadius + memberRadius)) * 0.5f;
		outLocation.getPosition().set(slotNumber * (memberRadius + memberRadius) - centerOffset, 0);
		return outLocation;
	}

	@Override
	public boolean supportsSlots(int slotCount) {
		return true;
	}

}
