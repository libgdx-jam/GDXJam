package com.gdxjam.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gdxjam.components.ResourceComponent.ResourceType;

public class ResourceTable extends Table{

	public ResourceType type;
	private Label label;
	
	public ResourceTable(ResourceType type, Skin skin){
		this.label = new Label(type.name + ": X / X" , skin);
		add(label);
	}
	
	public void update(int amount){
		label.setText(type.name + ": " + amount);
	}

}
