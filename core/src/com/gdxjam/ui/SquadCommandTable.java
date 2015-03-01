package com.gdxjam.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdxjam.ai.Squad;
import com.gdxjam.ai.formation.SquadFormationPattern;
import com.gdxjam.ai.formation.SquadFormationPattern.PatternType;
import com.gdxjam.ai.states.TaticalState;

public class SquadCommandTable extends Table{
	
	private final static Color selectedColor = new Color(1.0f, 0.8f, 0.75f, 0.85f);
	private final static Color defaultColor = new Color(0.7f, 0.7f, 0.7f, 0.85f);
	
	private SelectBox<TaticalState> taticalSelect;
	private SelectBox<PatternType> formationPatternSelect;
	private final Squad squad;
	private BitmapFontCache squadText;
	
	public SquadCommandTable(final Squad squad, Skin skin){
		this.squad = squad;
		setBackground(skin.getDrawable("default-window"));
		setColor(defaultColor);

		squadText = new BitmapFontCache(skin.getFont("default-font"));
		squadText.setMultiLineText("Squad " + (squad.index + 1), 0, 0);
		squadText.setColor(Color.WHITE);
		
		taticalSelect = new SelectBox<TaticalState>(skin);
		taticalSelect.setItems(TaticalState.values());
		
		formationPatternSelect = new SelectBox<PatternType>(skin);
		formationPatternSelect.setItems(PatternType.values());
		formationPatternSelect.setSelected(SquadFormationPattern.defaultPattern);
		formationPatternSelect.addListener(new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				squad.setFormationPattern(formationPatternSelect.getSelected());
				
			}
		});
		
		add(taticalSelect).pad(5);
		add(formationPatternSelect);
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
		float yPos = y + ((getHeight()) - (bounds.height * 0.5f));
		squadText.setPosition(xPos, yPos);
		squadText.draw(batch, parentAlpha);
	}
	
	
}
