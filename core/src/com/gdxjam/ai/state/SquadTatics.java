
package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;

public class SquadTatics {
	
	public static final float DEFENSIVE_RADIUS = 12;
	public static final float AGRESSIVE_RADIUS = 24;

	public enum Tatics {
		HARVEST(SquadHarvestState.IDLE),
		COMBAT(SquadCombatState.IDLE);
		
		public State<Entity> entryState;
		
		private Tatics(State<Entity> entryState){
			this.entryState = entryState;
		}
	}

}
