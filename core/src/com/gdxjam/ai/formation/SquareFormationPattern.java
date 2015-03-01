package com.gdxjam.ai.formation;

import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class SquareFormationPattern implements FormationPattern<Vector2>{

	private int numberOfSlots;
	private float memberRadius;
	private int columns;
	
	public SquareFormationPattern (float memberRadius) {
		this.memberRadius = memberRadius;
	}
	
	@Override
	public void setNumberOfSlots (int numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
		this.columns = (int)Math.sqrt(numberOfSlots);
	}

	@Override
	public Location<Vector2> calculateSlotLocation (Location<Vector2> outLocation, int slotNumber) {
		int x = slotNumber % columns;
		int y = slotNumber / columns;
		float memberDiameter = memberRadius + memberRadius;
		outLocation.getPosition().set(x * memberDiameter, y * memberDiameter);
		return outLocation;
	}

	@Override
	public boolean supportsSlots (int slotCount) {
		return true;
	}

}
