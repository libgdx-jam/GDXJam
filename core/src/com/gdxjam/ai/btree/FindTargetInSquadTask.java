package com.gdxjam.ai.btree;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.gdxjam.components.Components;
import com.gdxjam.components.UnitComponent;

public class FindTargetInSquadTask extends LeafTask<Entity>{

	@Override
	public void run (Entity entity) {
//		UnitComponent squadMemberComp = Components.SQUAD_MEMBER.get(entity);
	}

	@Override
	protected Task<Entity> copyTo (Task<Entity> task) {
		// TODO Auto-generated method stub
		return null;
	}

}
