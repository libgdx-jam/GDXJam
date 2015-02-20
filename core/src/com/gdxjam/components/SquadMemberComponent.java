package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.gdxjam.ai.Squad;
import com.gdxjam.ai.SteerableTarget;

public class SquadMemberComponent extends Component{
	
	public Squad squad;
	
	public SquadMemberComponent init(Squad squad){
		this.squad = squad;
		return this;
	}
	
	public SteerableTarget getTarget(){
		return squad.getTarget();
	}

}
