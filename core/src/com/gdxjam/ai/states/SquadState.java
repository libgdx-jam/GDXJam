package com.gdxjam.ai.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.gdxjam.components.Components;
import com.gdxjam.components.FactionComponent;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.utils.EntityUtils;

public enum SquadState implements State<Entity>{
	
	COMBAT(){
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
		}
		
		@Override
		public void update (Entity entity) {
			super.update(entity);
			TargetComponent targetComp = Components.TARGET.get(entity);
			if(targetComp.target == null){
				FactionComponent factionComp = Components.FACTION.get(entity);
				Entity enemySquad  = EntityUtils.findSquadWithoutFaction(factionComp.faction);
				targetComp.target = enemySquad;
				
				SquadComponent enemySquadComp = Components.SQUAD.get(enemySquad);
				SquadComponent squadComp = Components.SQUAD.get(entity);
				for(int i = 0; i < squadComp.members.size; i++){
					int targetIndex = squadComp.members.size % enemySquadComp.members.size;
					Entity member = squadComp.members.get(i);
					
					TargetComponent memberTargetComp = Components.TARGET.get(member);
					memberTargetComp.target = enemySquadComp.members.get(targetIndex);
				}

			}
		}
		
		
	};

	@Override
	public void enter (Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update (Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit (Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage (Entity entity, Telegram telegram) {
		// TODO Auto-generated method stub
		return false;
	}

}
