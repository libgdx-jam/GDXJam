package com.gdxjam.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.IntMap;
import com.gdxjam.Assets;

public class HotkeyTable extends Table {

	private final ImageTextButtonStyle hotkeyStyle;
	private InputProcessor inputProcessor;

	private final HorizontalGroup hotkeyRow = new HorizontalGroup();
	private final IntMap<Button> buttonMap = new IntMap<Button>();
	private final ButtonGroup<Button> buttonGroup = new ButtonGroup<Button>();

	public HotkeyTable() {
		this(new HotkeyTableStyle());
	}

	public HotkeyTable(HotkeyTableStyle style) {
		NinePatchDrawable draw = new NinePatchDrawable(Assets.getInstance().hotkey.button);

		hotkeyStyle = new ImageTextButtonStyle();
		hotkeyStyle.up = draw;
		hotkeyStyle.down = draw.tint(style.buttonSelectedColor);
		hotkeyStyle.checked = hotkeyStyle.down;
		hotkeyStyle.font = Assets.getInstance().fonts.medium;

		add(new Image(Assets.getInstance().hotkey.left));
		add(hotkeyRow);
		add(new Image(Assets.getInstance().hotkey.right));
	}

	/** adds a hotkey that calls keyDown with the keycode on the inputProcessor */
	public void addHotkey(final int keycode, final String label) {
		if (hotkeyRow.getChildren().size > 0) {
			hotkeyRow.addActor(new Image(Assets.getInstance().hotkey.middle));
		}
		final TextButton btn = new TextButton(label, hotkeyStyle);
		btn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (inputProcessor != null) {
					inputProcessor.keyDown(keycode);
				}
				return true;
			}
		});
		hotkeyRow.addActor(btn);
		buttonMap.put(keycode, btn);
		buttonGroup.add(btn);
	}

	/** keyDown will be called with the hotkey's keycode when a hotkey button is pressed */
	public void setInputProcessor(InputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
	}

	public void setChecked(int keycode) {
		if (buttonMap.containsKey(keycode)) {
			buttonMap.get(keycode).setChecked(true);
		}
	}

	public static class HotkeyTableStyle {
		public HotkeyTableStyle() {
		}

		public HotkeyTableStyle(Color buttonColor, Color buttonSelectedColor) {
			this.buttonColor = buttonColor;
			this.buttonSelectedColor = buttonSelectedColor;
		}

		public Color buttonColor = Color.WHITE, buttonSelectedColor = Color.GREEN;
	}
}
