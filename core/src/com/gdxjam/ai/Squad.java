package com.gdxjam.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.ai.formation.SquadFormationPattern;
import com.gdxjam.ai.formation.SquadFormationPattern.PatternType;
import com.gdxjam.utils.Location2;

public class Squad{

	public Formation<Vector2> formation;
	public FormationPattern<Vector2> pattern;
	public Location2 anchor;
	
	public int index = 0;
	
	public State<Entity> state;
	
	public boolean selected = false;

	public Squad(Vector2 position, int index) {
		this.index = index;
		this.anchor = new Location2(position);
		formation = new Formation<Vector2>(anchor, SquadFormationPattern.patterns.get(PatternType.Ring));
	}
	
	public void setFormationPattern(PatternType pattern){
		formation.setPattern(SquadFormationPattern.patterns.get(pattern));
		formation.updateSlotAssignments();
	}
	

	public void setTarget(Vector2 target) {
		this.anchor.getPosition().set(target);
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
