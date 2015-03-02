package com.gdxjam.ai.formation;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fma.SoftRoleSlotAssignmentStrategy.SlotCostProvider;
import com.badlogic.gdx.math.Vector2;

public class DistanceSlotCostProvider implements SlotCostProvider<Vector2> {

	@Override
	public float getCost(FormationMember<Vector2> member, int slotNumber) {
		return 0;
	}

}
