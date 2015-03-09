package com.gdxjam.ai.formation;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fma.SoftRoleSlotAssignmentStrategy.SlotCostProvider;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.UnitComponent;
import com.gdxjam.ecs.Components;

public class DistanceSlotCostProvider implements SlotCostProvider<Vector2> {
	
	@Override
	public float getCost(FormationMember<Vector2> member, int slotNumber) {
		UnitComponent unitComp = (UnitComponent) member;
		SquadComponent squadComp = Components.SQUAD.get(unitComp.getSquad());
		
		Vector2 targetPosition = squadComp.formation.getSlotAssignmentAt(slotNumber).member.getTargetLocation().getPosition();

		// The cost is the square distance between current position and target position
		return unitComp.getBody().getPosition().dst2(targetPosition);
	}
}
