package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.gdxjam.Assets;
import com.gdxjam.ai.Squad;
import com.gdxjam.ui.HotkeyTable;
import com.gdxjam.ui.HotkeyTable.HotkeyTableStyle;
import com.gdxjam.utils.Constants;

public class HUDSystem extends EntitySystem implements Disposable {

	private Stage stage;
	private HotkeyTable hotkeyTable;
	private final ObjectIntMap<Squad> squadKeyMap = new ObjectIntMap<Squad>(Constants.maxSquads);

	private Skin skin;

	public HUDSystem(Skin skin) {
		this.stage = new Stage();
		this.skin = skin;

		initGUI();
	}

	public void initGUI() {
		Table bottomTable = new Table();
		bottomTable.setFillParent(true);
		bottomTable.bottom();

		hotkeyTable = new HotkeyTable(new HotkeyTableStyle(Color.WHITE, Color.DARK_GRAY));
		bottomTable.add(hotkeyTable);
		stage.addActor(bottomTable);

		Table actionTableContainer = new Table();
		actionTableContainer.setFillParent(true);
		actionTableContainer.bottom().left();
		actionTableContainer.add(createActionTable());
		stage.addActor(actionTableContainer);
	}

	public Table createActionTable() {
		HotkeyTable table = new HotkeyTable();
		table.addHotkey(Keys.Q, "Q");
		table.addHotkey(Keys.W, "W");
		table.addHotkey(Keys.E, "E");
		table.addHotkey(Keys.R, "R");
		table.addHotkey(Keys.T, "T");
		return table;
	}

	public void addSquad(Squad squad) {
		String strIndex = String.valueOf(squad.index + 1);
		int keycode = Keys.valueOf(strIndex);
		hotkeyTable.addHotkey(keycode, strIndex);
		squadKeyMap.put(squad, keycode);
	}

	public void setSelected(Squad squad) {
		hotkeyTable.setChecked(squadKeyMap.get(squad, -1));
	}

	public void resize(int screenWidth, int screenHeight) {
		stage.getViewport().update(screenWidth, screenHeight);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public Stage getStage() {
		return stage;
	}

}
