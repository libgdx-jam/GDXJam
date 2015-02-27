package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.gdxjam.ai.Squad;

public class UnitComponent extends Component{
	
	public Squad squad; 
	public Entity target;
	public boolean isCommander; 

}
