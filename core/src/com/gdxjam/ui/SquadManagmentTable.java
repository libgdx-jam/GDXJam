
package com.gdxjam.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntMap;
import com.gdxjam.ai.Squad;

public class SquadManagmentTable extends Table {

	private final Skin skin;
	private IntMap<SquadCommandTable> squadTables;

	public SquadManagmentTable (Skin skin) {
		this.skin = skin;
		squadTables = new IntMap<SquadCommandTable>();

	}

	public void addSquad (Squad squad) {
		SquadCommandTable squadTable = new SquadCommandTable(squad, skin);
		squadTables.put(squad.index, squadTable);
		
		add(squadTable).pad(2);
	}
	
	public void updateSquadTable(Squad squad){
		squadTables.get(squad.index).update();
	}

	
	public void setSelected (int index, boolean selected) {
		squadTables.get(index).setSelected(selected);
	}

}
