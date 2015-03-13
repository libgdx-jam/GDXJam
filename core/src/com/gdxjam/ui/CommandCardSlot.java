package com.gdxjam.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class CommandCardSlot extends Table{
	
	protected final static Color defaultColor = new Color(0.66f, 0.66f, 0.66f, 0.44f);
	
	private CommandCard card;
	public int index;
	
	public CommandCardSlot(int index,Skin skin){
		this.index = index;
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
		
		card.setUserObject(index);
	}
	
	public CommandCard getCard(){
		return card;
	}
	
	public void setSelected(boolean selected){
		card.setSelected(selected);
	}
	
}
