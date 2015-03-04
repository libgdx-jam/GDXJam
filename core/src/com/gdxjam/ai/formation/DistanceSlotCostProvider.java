package com.gdxjam.ai.formation;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fma.SoftRoleSlotAssignmentStrategy.SlotCostProvider;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadMemberComponent;

public class DistanceSlotCostProvider implements SlotCostProvider<Vector2> {
	
	private static final Vector2 tmp = new Vector2();

	@Override
	public float getCost(FormationMember<Vector2> member, int slotNumber) {
		SquadMemberComponent squadMemberComp = (SquadMemberComponent) member;
		SquadComponent squadComp = Components.SQUAD.get(squadMemberComp.squad);
		
		tmp.set(squadComp.formation.getSlot(slotNumber).member.getTargetLocation().getPosition());
		float cost = squadMemberComp.body.getPosition().dst(tmp);
		return cost;
		
//		return 0;
	
	}
}
