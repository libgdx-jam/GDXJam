package com.gdxjam.components;

import com.badlogic.ashley.core.Component;

public class FactionComponent extends Component {

	public enum Faction {
		FACTION0("The Player (sometimes?)"),
		FACTION1("The enemy (sometimes!)"),
		NONE("Neutral");
		
		public String name;
		private Faction(String name){
			this.name = name;
		}
	}
	
	public Faction faction;
}
