package com.gdxjam.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdxjam.GameManager;

public class PauseDialog extends Dialog{

	public PauseDialog (final Skin skin) {
		super("Paused", skin);
		
		TextButton optionsButton = new TextButton("Options", skin);
		optionsButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				OptionsDialog dialog = new OptionsDialog(skin);
				dialog.show(getStage());
			}
		});

		TextButton closeButton = new TextButton("Close", skin);
		closeButton.addListener(new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				GameManager.resume();
				hide();
			}
		});
		
		getButtonTable().add(optionsButton);
		getButtonTable().row();
		getButtonTable().add(closeButton);
		
	}
	
}
