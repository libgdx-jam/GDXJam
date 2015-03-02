package com.gdxjam.ai.formation;

import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.utils.Vector2Utils;

public class VFormationPattern implements FormationPattern<Vector2> {

	private int numberOfSlots;
	private float memberRadius;
	private float angle;

	private Vector2 leftBorder = new Vector2();
	private Vector2 rightBorder = new Vector2();

	public VFormationPattern(float angle, float memberRadius) {
		this.memberRadius = memberRadius;
		setAngle(angle);
	}

	public float getMemberRadius() {
		return memberRadius;
	}

	public void setMemberRadius(float memberRadius) {
		this.memberRadius = memberRadius;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
		Vector2Utils.angleToVector(leftBorder, angle);
		Vector2Utils.angleToVector(rightBorder, -angle);
	}

	@Override
	public boolean supportsSlots(int slotCount) {
		return true;
	}

	@Override
	public void setNumberOfSlots(int numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
	}

	@Override
	public Location<Vector2> calculateSlotLocation(
			Location<Vector2> outLocation, int slotNumber) {
		Vector2 border = ((slotNumber + 1) % 2) == 0 ? leftBorder : rightBorder;
		float radius = ((slotNumber + 1) / 2) * (memberRadius + memberRadius);
		outLocation.getPosition().set(border).scl(radius);
		outLocation.setOrientation(0);
		return outLocation;
	}

}
