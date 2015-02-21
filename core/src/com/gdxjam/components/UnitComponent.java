package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.gdxjam.ai.Squad;
import com.gdxjam.components.ResourceComponent.ResourceType;

public class UnitComponent extends Component{
	
	public Squad squad; 
	public boolean isCommander; 
	
	public ResourceType assignedResource;

}
