package com.gdxjam.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.formation.SquadFormationPattern;
import com.gdxjam.ai.formation.SquadFormationPattern.PatternType;
import com.gdxjam.components.Components;
import com.gdxjam.utils.Location2;

public class Squad implements Steerable<Vector2>{

	public Formation<Vector2> formation;
	public Location2 anchor;
	public Array<Entity> members;
	
	public int index = 0;
	
	public State<Entity> state;
	
	public boolean selected = false;

	public Squad(Vector2 position, int index) {
		this.index = index;
		this.anchor = new Location2(position);
		formation = new Formation<Vector2>(anchor, SquadFormationPattern.patterns.get(PatternType.Ring));
		members = new Array<Entity>();
	}
	
	public void addMember(Entity entity){
		members.add(entity);
		formation.addMember(Components.SQUAD_MEMBER.get(entity));
	}

	public void setMemberState(State<Entity> state){
		for(Entity entity : members){
			Components.STATE_MACHINE.get(entity).stateMachine.changeState(state);
		}
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

	@Override
	public Vector2 getPosition () {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getOrientation () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setOrientation (float orientation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float vectorToAngle (Vector2 vector) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2 angleToVector (Vector2 outVector, float angle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location<Vector2> newLocation () {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getMaxLinearSpeed () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxLinearSpeed (float maxLinearSpeed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getMaxLinearAcceleration () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxLinearAcceleration (float maxLinearAcceleration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getMaxAngularSpeed () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxAngularSpeed (float maxAngularSpeed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getMaxAngularAcceleration () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxAngularAcceleration (float maxAngularAcceleration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector2 getLinearVelocity () {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getAngularVelocity () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getBoundingRadius () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isTagged () {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTagged (boolean tagged) {
		// TODO Auto-generated method stub
		
	}
	
}
