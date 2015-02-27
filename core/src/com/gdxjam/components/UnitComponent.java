package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.ai.Squad;

public class UnitComponent extends Component{
	
	public Squad squad; 
	public Entity target;
	public boolean isCommander;
	
}
