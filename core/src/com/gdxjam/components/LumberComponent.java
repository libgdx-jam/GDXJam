package com.gdxjam.components;

public class LumberComponent extends ResourceComponent{
	
		public LumberComponent () {
			resourceType = ResourceType.WOOD;
		}
		
		public LumberComponent init(int amount){
			this.amount = amount;
			return this;
		}

}
