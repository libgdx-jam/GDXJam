
package com.gdxjam.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gdxjam.utils.Constants;

public class GameTimeTable extends Table {

	private Label timeLabel;

	public GameTimeTable (Skin skin) {
		timeLabel = new Label("XX : XX", skin);
		add(timeLabel);
		
	}

	public void update (float time) {
		int hour = (int)(time / Constants.secondsPerHour);
		int minute = (int)((time - (hour * Constants.secondsPerHour)) / Constants.secondsPerMinute);

		if (minute < 10) {
			timeLabel.setText(hour + ":0" + minute);
		} else {
			timeLabel.setText(hour + ":" + minute);
		}
	}


}
