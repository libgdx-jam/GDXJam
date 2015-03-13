package com.gdxjam.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdxjam.GameManager;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.utils.Constants;

public class EmptyCommandCard extends CommandCard{
	
	private int index;

	public EmptyCommandCard (final int index, Skin skin) {
		super(index, skin);
		
		TextButton button = new TextButton("Add new SquadButton!", skin);
		button.addListener(new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				GameManager.getEngine().getSystem(SquadSystem.class).createPlayerSquad(index, new Vector2(128, 128), Constants.playerFaction, 1);
			}
		});
		add(button);
	}

	@Override
	public void setSelected (boolean selected) {
		// TODO Auto-generated method stub
		
	}
	

	

}
