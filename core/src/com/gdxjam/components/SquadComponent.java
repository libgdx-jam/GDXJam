package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.fma.SoftRoleSlotAssignmentStrategy;
import com.badlogic.gdx.ai.fma.patterns.OffensiveCircleFormationPattern;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.formation.ColumnFormationPattern;
import com.gdxjam.ai.formation.DistanceSlotCostProvider;
import com.gdxjam.ai.formation.LineFormationPattern;
import com.gdxjam.ai.formation.SquareFormationPattern;
import com.gdxjam.ai.formation.VFormationPattern;
import com.gdxjam.ai.formation.WedgeFormationPattern;
import com.gdxjam.ai.states.SquadState;
import com.gdxjam.ai.states.UnitState;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.Location2;

public class SquadComponent extends Component{
	
	public enum PatternType {
		Line,
		Column,
		Square,
		Ring,
		V,
		Wedge;
	}
	
	public static final float PATTERN_SPACING = Constants.unitRadius * 0.25f;
	public static final PatternType DEFAULT_PATTERN = PatternType.V;
	public static final SquadState DEFAULT_STATE = SquadState.COMBAT;
	
	public Array<Entity> members;
	public Formation<Vector2> formation;
	public Location2 targetLocation;
	public State<Entity> state = UnitState.FORMATION;

	public boolean selected = false;
	
	public SquadComponent(){
		members = new Array<Entity>();
		targetLocation = new Location2();
	}
	
	public SquadComponent init(Steerable<Vector2> steerable){
		SoftRoleSlotAssignmentStrategy<Vector2> slotAssignmentStrategy = new SoftRoleSlotAssignmentStrategy<Vector2>(new DistanceSlotCostProvider());
		formation = new Formation<Vector2>(steerable, getFormationPattern(DEFAULT_PATTERN), slotAssignmentStrategy);
		return this;
	}
	
	
	public void addMember(Entity entity){
		members.add(entity);
		formation.addMember(Components.SQUAD_MEMBER.get(entity));
//		formation.updateSlotAssignments();
	}
	
	public void removeMember(Entity entity){
		members.removeValue(entity, true);
		formation.removeMember(Components.SQUAD_MEMBER.get(entity));
		//formation.updateSlotAssignments();
	}
	
	public void setState(State<Entity> state){
		this.state = state;
		for(Entity entity : members){
			Components.STATE_MACHINE.get(entity).stateMachine.changeState(state);
		}
	}
	
	public void setFormationPattern(PatternType pattern){
		FormationPattern<Vector2> formationPattern = getFormationPattern(pattern);
		formation.setPattern(formationPattern);
		formation.updateSlotAssignments();
	}
	
	public FormationPattern<Vector2> getFormationPattern(PatternType pattern){
		switch(pattern){
		case Line:
			return new LineFormationPattern(Constants.unitRadius + PATTERN_SPACING);
		case Column:
			return new ColumnFormationPattern(Constants.unitRadius + PATTERN_SPACING);
		case Square:
			return new SquareFormationPattern(Constants.unitRadius + PATTERN_SPACING);
		default:
		case Ring:
			return new OffensiveCircleFormationPattern<Vector2>(Constants.unitRadius + PATTERN_SPACING);
		case V:
			return new VFormationPattern(60 * MathUtils.degreesToRadians, Constants.unitRadius + PATTERN_SPACING);
		case Wedge:
			return new WedgeFormationPattern(Constants.unitRadius + PATTERN_SPACING);
		}
	}
	

	public void setTarget(Vector2 target) {
		targetLocation.getPosition().set(target);
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
