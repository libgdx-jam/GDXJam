
package com.gdxjam.ai.btree.task;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.gdxjam.components.UnitComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.ecs.Components;

public class HasTargetCondition extends LeafTask<Entity> {

	boolean checkSquad = false;
	
	@Override
	public void run (Entity entity) {
		boolean validTarget = false;
		Entity findTarget = entity;
		if(checkSquad){
//			UnitComponent squadMemberComp = Components.SQUAD_MEMBER.get(entity);
//			findTarget = squadMemberComp.squad;
		}
		

		if (Components.TARGET.has(findTarget)) {
			TargetComponent targetComp = Components.TARGET.get(findTarget);
			if (targetComp.getTarget() != null) {
				validTarget = true;
			}
		}
		if (validTarget) {
			success();
		} else {
			fail();
		}
	}

	@Override
	protected Task<Entity> copyTo (Task<Entity> task) {
		// TODO Auto-generated method stub
		return null;
	}

}
