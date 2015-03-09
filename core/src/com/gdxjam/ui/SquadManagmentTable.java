
package com.gdxjam.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntMap;

public class SquadManagmentTable extends Table {

	private final Skin skin;
	private IntMap<SquadCommandCard> squadTables;

	public SquadManagmentTable (Skin skin) {
		this.skin = skin;
		squadTables = new IntMap<SquadCommandCard>();

	}

	public void addSquad (Entity squad, int index) {
		SquadCommandCard squadTable = new SquadCommandCard(squad, index, skin);
		squadTables.put(index, squadTable);
		
		add(squadTable).pad(2);
	}
	
	public void removeSquad(Entity squad, int index){
		SquadCommandCard table = squadTables.get(index);
		table.remove();
		
		squadTables.remove(index);
	}
	
	public void updateSquadTable(int index){
		squadTables.get(index).update();
	}

	
	public void setSelected (int index, boolean selected) {
		squadTables.get(index).setSelected(selected);
	}

}
