package com.gdxjam.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdxjam.GameManager;
import com.gdxjam.ai.state.SquadTatics.Tatics;
import com.gdxjam.ai.state.TelegramMessage;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadComponent.FormationPatternType;
import com.gdxjam.ecs.Components;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.utils.Constants;

public class SquadCommandCard extends CommandCard{

	private SelectBox<Tatics> tatics;
	private Entity squad;
	private BitmapFontCache squadText;
	
	private FormationPatternTable formationTable;
	
	public SquadCommandCard(final Entity squad, int index, Skin skin){
		super(index, skin);
		this.squad = squad;

		squadText = new BitmapFontCache(skin.getFont("default-font"));
		
		setSquad(squad);
	}
	
	public void setSquad(final Entity squad){
		this.squad = squad;
		
		if(squad == null){
			setEmpty();
			return;
		}

		int index = (Integer)getUserObject();
		squadText.setMultiLineText("Squad " + (index + 1), 0, 0);
		squadText.setColor(Color.WHITE);

		tatics = new SelectBox<Tatics>(skin);
		tatics.setItems(Tatics.values());
		tatics.setSelected(Tatics.COMBAT);
		tatics.addListener(new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				Components.FSM.get(squad).changeState(tatics.getSelected().entryState);
			}
		});
		
		formationTable = new FormationPatternTable(squad, skin);
		
		TextButton addMemberButton = new TextButton(" + ", skin);
		addMemberButton.addListener(new ChangeListener(){
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				MessageManager.getInstance().dispatchMessage(TelegramMessage.CONSTRUCT_UNIT_REQUEST.ordinal(), squad);
			}
		});
		
		add(tatics).pad(5);
		add(addMemberButton);
		row();
		add(formationTable).colspan(2);
	}
	
	public void setEmpty(){
		reset();
		TextButton button = new TextButton("Add new SquadButton!", skin);
		button.addListener(new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				int index = (Integer)getUserObject();
				GameManager.getEngine().getSystem(SquadSystem.class).createPlayerSquad(index, new Vector2(128, 128), Constants.playerFaction, 1);
			}
		});
		add(button);
	}
	
	public void updateFormationPattern(FormationPatternType pattern){
		formationTable.updateFormationPattern(pattern);
	}
	

	public Entity getSquad(){
		return squad;
	}
	
	public void update(){
		SquadComponent squadComp = Components.SQUAD.get(squad);
		int index = (Integer)getUserObject();
		squadText.setMultiLineText("Squad " + (index + 1) + "   (" + squadComp.members.size + " / " + Constants.maxSquadMembers + ")", 0, 0);
	}
	
	public void setSelected(boolean selected){
		squadText.tint(selected ? Color.CYAN : Color.WHITE);
		setColor(selected ? selectedColor : defaultColor);
	}
	
	@Override
	protected void drawBackground (Batch batch, float parentAlpha, float x, float y) {
		super.drawBackground(batch, parentAlpha, x, y);
		TextBounds bounds = squadText.getBounds();
		float xPos = x + ((getWidth() * 0.5f) - (bounds.width * 0.5f));
		float yPos = y + ((getHeight()) - (bounds.height * 0.5f) ) + 4;	//The constant should not be needed
		squadText.setPosition(xPos, yPos);
		squadText.draw(batch, parentAlpha);
	}
	
	
}
