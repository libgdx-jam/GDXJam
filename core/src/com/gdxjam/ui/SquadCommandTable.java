package com.gdxjam.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdxjam.ai.states.SquadState;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadComponent.PatternType;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;

public class SquadCommandTable extends Table{
	
	private final static Color selectedColor = new Color(240.0f / 255.0f, 230.0f / 255.0f, 140.0f / 255.0f, 0.85f);
	private final static Color defaultColor = new Color(0.66f, 0.66f, 0.66f, 0.85f);
	
	private SelectBox<SquadState> squadState;
	private SelectBox<PatternType> formationPatternSelect;
	private final Entity squad;
	private final int index;
	private BitmapFontCache squadText;
	

	public SquadCommandTable(final Entity squad, int index, Skin skin){
		this.squad = squad;
		this.index = index;
		setBackground(skin.getDrawable("default-window"));
		setColor(defaultColor);

		squadText = new BitmapFontCache(skin.getFont("default-font"));
		squadText.setMultiLineText("Squad " + (index + 1), 0, 0);
		squadText.setColor(Color.WHITE);

		squadState = new SelectBox<SquadState>(skin);
		squadState.setItems(SquadState.values());
		squadState.setSelected(SquadComponent.DEFAULT_STATE);
		squadState.addListener(new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				Components.STATE_MACHINE.get(squad).stateMachine.changeState(squadState.getSelected());
			}
		});
		
		formationPatternSelect = new SelectBox<PatternType>(skin);
		formationPatternSelect.setItems(PatternType.values());
		formationPatternSelect.setSelected(SquadComponent.DEFAULT_PATTERN);
		formationPatternSelect.addListener(new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				Components.SQUAD.get(squad).setFormationPattern(formationPatternSelect.getSelected());
			}
		});
		
		TextButton addMemberButton = new TextButton(" + ", skin);
		addMemberButton.addListener(new ChangeListener(){
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				EntityFactory.createUnit(squad);
			}
		});
		
		add(squadState).pad(5);
		row();
		add(formationPatternSelect);
		row();
		add(addMemberButton);
	}
	
	public void update(){
		SquadComponent squadComp = Components.SQUAD.get(squad);
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
