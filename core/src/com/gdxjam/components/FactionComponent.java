package com.gdxjam.components;

import com.badlogic.ashley.core.Component;

public class FactionComponent extends Component{

	public enum Faction {
		FACTION0("Republic", "This is a test so you know"),
		FACTION1("Alien", "So just ignore this for now"),
		FACTION2("Industrialist", "But I think its working so you know."),
		NONE("Neutral", "You will never see this");
		
		public String name;
		public String description;
		
		private Faction(String name, String description){
			this.name = name;
			this.description = description;
		}
	}
	
	private Faction faction;
	
	/** Can only be created by PooledEngine */
	private FactionComponent () { 
		// private constructor
	}
	
	public FactionComponent init(Faction faction){
		this.faction = faction;
		return this;
	}
	
	public Faction getFaction(){
		return faction;
	}
	
	
}
