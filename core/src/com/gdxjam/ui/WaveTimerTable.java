
package com.gdxjam.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class WaveTimerTable extends Table {

	Label label;

	public WaveTimerTable (Skin skin) {
		label = new Label("XX : XX", skin);
		add(label);
	}

	public void update (float timeRemaining) {
		int minutes = (int)(timeRemaining / 60);
		int seconds = (int)((timeRemaining - (minutes * 60)));

		if(seconds < 10)
			label.setText("Next Raid " + minutes + ":0" + seconds);
		else
			label.setText("Next Raid " + minutes + ":" + seconds);
		
		if(timeRemaining < 10.0f)
			label.setColor(Color.RED);
		else
			label.setColor(Color.WHITE);
		
	}

}
