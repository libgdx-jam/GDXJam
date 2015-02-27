package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.Squad;
import com.gdxjam.components.Components;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.UnitComponent;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;

public class SquadSystem extends EntitySystem{
	
	private static final String TAG = "[" + SquadSystem.class.getSimpleName() +"]";
	public Array<Squad> squads;
	
	private int population;
	
	private HUDSystem hudSystem;
	private ResourceSystem resourceSystem;
	
	public SquadSystem(){
		squads = new Array<Squad>(true, Constants.maxSquads);
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		hudSystem = engine.getSystem(HUDSystem.class);
		resourceSystem = engine.getSystem(ResourceSystem.class);
	}
	
	public void createUnit(Vector2 position){
		EntityFactory.createUnit(position);
		population++;
		hudSystem.updatePopulation(population);
	}
	
	public void killUnit(Entity entity){
		population--;
		hudSystem.updatePopulation(population);
	}
	
	public Squad createSquad(Entity commander){
		SteerableBodyComponent steerable = Components.STEERABLE_BODY.get(commander);
		int index = squads.size;
		Squad squad = new Squad(steerable.getPosition().cpy(), index);
		
		addUnitToSquad(commander, squad);
		squads.add(squad);
		hudSystem.addSquad(squad);
		
		return squad;
	}
	
	public void setTarget(Vector2 target){
		for (Squad squad : squads) {
			if (squad.isSelected()) {
				squad.setTarget(target);
			}
		}
	}
	
	public void setState(State<Entity> state){
		for(Squad squad : squads){
			if(squad.selected){
				setState(state, squad);
			}
		}
	}
	
	public void setState(State<Entity> state, Squad squad){
		squad.state = state;
		for(Entity entity : squad.entities){
			Components.STATE_MACHINE.get(entity).stateMachine.changeState(state);
		}
	}
	
	public boolean toggleSelected(Squad squad){
		squad.selected = !squad.selected;
		hudSystem.setSelected(squad);
		return squad.selected;
	}
	
	public boolean toggleSelected(int index){
		if(squads.size > index)
			return toggleSelected(squads.get(index));
		return false;
	}
	
	public Squad addUnitToSquad(Entity entity, Squad squad){
		if(Components.UNIT.has(entity)){
			UnitComponent unit = Components.UNIT.get(entity);
			unit.squad = squad;
			
			squad.addEntity(entity);
			Components.STATE_MACHINE.get(entity).stateMachine.changeState(squad.state);
		}
		else{
			Gdx.app.error(TAG, "entity cannot be added to squad " + squad.index + " : Entity is not a unit");
		}
		return squad;
	}
	
	public Array<Squad> getSquads(){
		return squads;
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
//		if(squads.size > 0){
//			squads.get(0);
//		}
	}

}
