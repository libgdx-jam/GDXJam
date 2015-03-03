package com.gdxjam.ai.formation;

import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class WedgeFormationPattern implements FormationPattern<Vector2> {

	private int numberOfSlots;
	private float memberRadius;

	public WedgeFormationPattern(float memberRadius) {
		this.memberRadius = memberRadius;
	}

	@Override
	public void setNumberOfSlots(int numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
	}

	@Override
	public Location<Vector2> calculateSlotLocation(
			Location<Vector2> outLocation, int slotNumber) {
		int row = calculateRow(slotNumber);
		float col = calculateColumn(slotNumber, row);
		float memberDiameter = memberRadius + memberRadius;
		outLocation.getPosition().set(-row * memberDiameter, -col * memberDiameter);
		outLocation.setOrientation(0);
		return outLocation;
	}

	@Override
	public boolean supportsSlots(int slotCount) {
		return true;
	}

	private int calculateRow(int slotNumber) {
		double r = (Math.sqrt(1 + 8 * (slotNumber+1)) - 1) * .5;
		return (int)Math.ceil(r) - 1;
	}
	
	private float calculateColumn(int slotNumber, int row) {
		int r = row * (row + 1) / 2;
		return slotNumber - r - row * .5f;
	}

}
