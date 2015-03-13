package com.gdxjam.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CommandCard extends Table{
	
	protected final static Color selectedColor = new Color(240.0f / 255.0f, 230.0f / 255.0f, 140.0f / 255.0f, 0.85f);
	protected final static Color defaultColor = new Color(0.66f, 0.66f, 0.66f, 0.85f);
	
	protected Skin skin;
	protected boolean selected = false;
	public CommandCard(int index, Skin skin){
		setBackground(skin.getDrawable("default-window"));
		setColor(defaultColor);
		setTouchable(Touchable.enabled);
		
		setUserObject(index);
		this.skin = skin;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
		setColor(selected ? selectedColor : defaultColor);
	}
	
	public boolean isSelected(){
		return selected;
	}

}
