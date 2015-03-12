package com.gdxjam.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class CommandCardSlot extends Table{
	
	protected final static Color defaultColor = new Color(0.66f, 0.66f, 0.66f, 0.44f);
	
	private CommandCard card;
	private boolean selected;
	
	public CommandCardSlot(Skin skin){
		Drawable background = skin.getDrawable("default-round");
		setBackground(background);
		setTouchable(Touchable.enabled);
	}
	
	public void setCard(CommandCard card){
		this.card = card;
		card.remove();
		reset();
		
		add(card);
		setUserObject(card);
		
		card.setSelected(selected);
		card.setUserObject(this);
	}
	
	public CommandCard getCard(){
		return card;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
		card.setSelected(selected);
	}
	
}
