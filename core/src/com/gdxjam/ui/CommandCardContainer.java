
package com.gdxjam.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;

public class CommandCardContainer extends Table {

	private final Skin skin;
	private IntMap<SquadCommandCard> squadCards;
	private EmptyCommandCard emptyCard;
	
	public CommandCardContainer (Skin skin) {
		this.skin = skin;
		squadCards = new IntMap<SquadCommandCard>();
		emptyCard = new EmptyCommandCard(skin);
	}

	public void addSquad (Entity squad, int index) {
		SquadCommandCard squadTable = new SquadCommandCard(squad, index, skin);
		squadCards.put(index, squadTable);
		
		repack();
	}
	
	public void repack(){
		reset();
		
		for(Entry<SquadCommandCard> entry : squadCards){
			entry.value.remove();
			add(entry.value);
		}
		
		if(squadCards.size < 5){
			add(emptyCard);
		}
	}
	
	public void removeSquad(Entity squad, int index){
		squadCards.remove(index);
		repack();
	}
	
	public void updateSquadTable(int index){
		squadCards.get(index).update();
	}

	
	public void setSelected (int index, boolean selected) {
		squadCards.get(index).setSelected(selected);
	}

}
