package com.gdxjam.ai.formation;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fma.SoftRoleSlotAssignmentStrategy.SlotCostProvider;
import com.badlogic.gdx.math.Vector2;

public class DistanceSlotCostProvider implements SlotCostProvider<Vector2>{

	@Override
	public void calculateCosts () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getCost (FormationMember<Vector2> member, int slotNumber) {
		boolean isCorrectSlot = slotNumber % 2 == 0;
		float cost = isCorrectSlot ? 0f : 10000f * 2;
		return cost + 1;
	}

}
