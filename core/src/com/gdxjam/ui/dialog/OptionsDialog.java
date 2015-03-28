
package com.gdxjam.ui.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gdxjam.GameManager;
import com.gdxjam.GameManager.GameConfig;
import com.gdxjam.OrionPrefs;
import com.gdxjam.OrionPrefs.BooleanValue;
import com.gdxjam.OrionPrefs.FloatValue;
import com.gdxjam.OrionPrefs.StringValue;

public class OptionsDialog extends Dialog {
	
	private static final int defaultContentWidth = 300;
	private static final int defaultContentHeight = 100;

	private GraphicsOptions graphicsOptions;
	private AudioOptions audioOptions;
	
	private ButtonGroup<TextButton> tabButtonGroup;
	private Table tabTable;

	public OptionsDialog (Skin skin) {
		super("Options", skin);
		setPosition((Gdx.graphics.getWidth() * 0.5f) - (getWidth() * 0.5f), (Gdx.graphics.getHeight() * 0.5f)
			- (getHeight() * 0.5f));
		setMovable(false);
		setModal(true);

		
		graphicsOptions = new GraphicsOptions(skin);
		audioOptions = new AudioOptions(skin);
		
		tabButtonGroup = new ButtonGroup<TextButton>();
		tabButtonGroup.setMaxCheckCount(1);
		tabButtonGroup.setMinCheckCount(1);
		tabButtonGroup.setChecked(graphicsOptions.getName());
		
		tabTable = new Table();
		addTab(graphicsOptions, skin);
		addTab(audioOptions, skin);
		setContent(graphicsOptions);
		
		TextButton closeButton = new TextButton("Close", skin);
		button(closeButton);
	}
	
	public void addTab(final Table content, Skin skin){
		TextButton tabButton = new TextButton(content.getName(), skin);
		tabButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				setContent(content);
			}
		});
		tabTable.add(tabButton);
	}
	
	public void setContent(Table tabContent){
		getContentTable().clear();
		getContentTable().add(tabTable);
		getContentTable().row();
		getContentTable().add(tabContent).prefSize(defaultContentWidth, defaultContentHeight);
	}

	public class GraphicsOptions extends Table {

		private SelectBox<String> resolutionSelectBox;
		private CheckBox fullscreenCheckBox;

		public GraphicsOptions (Skin skin) {
			setName("Graphics");
			resolutionSelectBox = new SelectBox<String>(skin);
			resolutionSelectBox.setItems(GameConfig.SUPPORTED_RESOLUTIONS);
			resolutionSelectBox.setSelected(OrionPrefs.getString(StringValue.GRAPHICS_RESOLUTION));

			fullscreenCheckBox = new CheckBox("Fullscreen", skin);
			fullscreenCheckBox.setChecked(OrionPrefs.getBoolean(BooleanValue.GRAPHICS_FULLSCREEN));

			TextButton applyButton = new TextButton("Apply", skin);
			applyButton.addListener(new ChangeListener() {

				@Override
				public void changed (ChangeEvent event, Actor actor) {
					OrionPrefs.putBoolean(BooleanValue.GRAPHICS_FULLSCREEN, fullscreenCheckBox.isChecked());
					OrionPrefs.putString(StringValue.GRAPHICS_RESOLUTION, resolutionSelectBox.getSelected());
					GameManager.refreshDisplayMode();
				}
			});

			add(resolutionSelectBox);
			row();
			add(fullscreenCheckBox);
			row();
			add(applyButton);
		}
	}

	public class AudioOptions extends Table {
		
		private Slider soundSlider;
		private Slider musicSlider;
		
		private CheckBox soundCheckBox;
		private CheckBox musicCheckBox;

		public AudioOptions (Skin skin) {
			setName("Audio");
			soundSlider = new Slider(0, 1, 0.05f, false, skin);
			soundSlider.setValue(OrionPrefs.getFloat(FloatValue.AUDIO_SOUND_VOLUME));
			musicSlider = new Slider(0, 1, 0.05f, false, skin);
			musicSlider.setValue(OrionPrefs.getFloat(FloatValue.AUDIO_MUSIC_VOLUME));
			
			musicCheckBox = new CheckBox("Music Enabled", skin);
			musicCheckBox.setChecked(OrionPrefs.getBoolean(BooleanValue.AUDIO_MUSIC_ENABLED));
			soundCheckBox = new CheckBox("Sound Enabled", skin);
			soundCheckBox.setChecked(OrionPrefs.getBoolean(BooleanValue.AUDIO_MUSIC_ENABLED));
			
			Table soundTable = new Table();
			soundTable.add(soundSlider);
			soundTable.add(soundCheckBox);
			
			Table musicTable = new Table();
			musicTable.add(musicSlider);
			musicTable.add(musicCheckBox);
			
			add(soundTable);
			row();
			add(musicTable);
			
		}

	}

}
