package com.gdxjam.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PopulationTable extends Table{
	
	private Label population;
	
	public PopulationTable(Skin skin){
		population = new Label("Population", skin);
		add(population);
	}
}
