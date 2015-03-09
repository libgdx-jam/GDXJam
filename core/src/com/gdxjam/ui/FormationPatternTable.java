package com.gdxjam.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.gdxjam.Assets;
import com.gdxjam.components.SquadComponent.PatternType;
import com.gdxjam.ecs.Components;

public class FormationPatternTable extends Table{
	
	private ButtonGroup<ImageButton> buttonGroup;
	
	public FormationPatternTable (final Entity squad, Skin skin) {
		buttonGroup = new ButtonGroup<ImageButton>();
		buttonGroup.setMaxCheckCount(1);
		buttonGroup.setMinCheckCount(1);
		
		for(int i = 0; i < PatternType.values().length; i++){
			final ImageButton button = createFormationButton(PatternType.values()[i], skin);
			button.addListener(new ChangeListener() {
			
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					if(button.isChecked()){
						Components.SQUAD.get(squad).setFormationPattern((PatternType) button.getUserObject());
					}
					
				}
			});
			
			
			add(button);
			buttonGroup.add(button);
		}
	}
	
	private ImageButton createFormationButton(PatternType pattern, Skin skin){
		ImageButtonStyle style = new ImageButtonStyle(skin.get("default", ButtonStyle.class));
		
		Sprite sprite = new Sprite(Assets.ui.formationIcons.get(pattern.ordinal()));
		sprite.setSize(26, 26);
		sprite.setColor(Color.BLUE);
		SpriteDrawable drawable = new SpriteDrawable(sprite);
		
		style.imageUp = drawable;
//		style.imageDown = drawable.tint(Color.CYAN);
		style.imageChecked = drawable.tint(Color.RED);
		
		ImageButton button = new ImageButton(style);
		button.setUserObject(pattern);
		return button;
	}

}