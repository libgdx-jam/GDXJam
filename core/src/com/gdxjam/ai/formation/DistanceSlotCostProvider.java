package com.gdxjam.ai.formation;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fma.SoftRoleSlotAssignmentStrategy.SlotCostProvider;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadMemberComponent;

public class DistanceSlotCostProvider implements SlotCostProvider<Vector2> {
	
	@Override
	public float getCost(FormationMember<Vector2> member, int slotNumber) {
		SquadMemberComponent squadMemberComp = (SquadMemberComponent) member;
		SquadComponent squadComp = Components.SQUAD.get(squadMemberComp.squad);
		
		Vector2 targetPosition = squadComp.formation.getSlot(slotNumber).member.getTargetLocation().getPosition();

		// The cost is the square distance between current position and target position
		return squadMemberComp.body.getPosition().dst2(targetPosition);
	}
}
