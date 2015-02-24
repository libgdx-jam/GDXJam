package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.gdxjam.ai.Squad;
import com.gdxjam.components.ResourceComponent.ResourceType;
import com.gdxjam.ui.GameTimeTable;
import com.gdxjam.ui.HotkeyTable;
import com.gdxjam.ui.HotkeyTable.HotkeyTableStyle;
import com.gdxjam.ui.ResourceGroup;
import com.gdxjam.utils.Constants;

public class HUDSystem extends EntitySystem implements Disposable {

	private Stage stage;
	private Skin skin;
	private HotkeyTable hotkeyTable, actionTable;
	private GameTimeTable gameTimeTable;
	
	private final ObjectIntMap<Squad> squadKeyMap = new ObjectIntMap<Squad>(Constants.maxSquads);
	private ResourceGroup resources;

	public HUDSystem(Skin skin) {
		this.stage = new Stage();
		this.skin = skin;

		initGUI();

		stage.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				hotkeyTable.setChecked(keycode);
				actionTable.setChecked(keycode);
				return false;
			}

			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				return false;
			}
		});
	}

	public void initGUI() {
		Table bottomTable = new Table();
		bottomTable.setFillParent(true);
		bottomTable.bottom();

		hotkeyTable = new HotkeyTable(new HotkeyTableStyle(Color.WHITE, Color.DARK_GRAY));
		actionTable = createActionTable();

		bottomTable.add(hotkeyTable);
		stage.addActor(bottomTable);

		Table actionTableContainer = new Table();
		actionTableContainer.setFillParent(true);
		actionTableContainer.bottom().left();
		actionTableContainer.add(actionTable);
		stage.addActor(actionTableContainer);
		
		//Game time table
		gameTimeTable = new GameTimeTable(skin);

		
		/** Resource Table		 */
		resources = new ResourceGroup(skin);
		
		Table topTable = new Table();
		topTable.setFillParent(true);
		topTable.top().right();
		
		topTable.defaults().pad(5);
		topTable.add(resources);
		topTable.add(gameTimeTable);

		stage.addActor(topTable);
		
	}

	public HotkeyTable createActionTable() {
		HotkeyTable table = new HotkeyTable();
		table.addHotkey(Keys.Q, "Q");
		table.addHotkey(Keys.W, "W");
		table.addHotkey(Keys.E, "E");
		table.addHotkey(Keys.R, "R");
		table.addHotkey(Keys.T, "T");
		return table;
	}

	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
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
	
	public void updateResource(ResourceType type, int amount){
		resources.update(type, amount);
	}
	
	public void updatePopulation(float population){
		//Population
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		stage.act();
		stage.draw();
	}

	public Stage getStage() {
		return stage;
	}

//	public HotkeyTable getHotkeyTable() {
//		return hotkeyTable;
//	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

}
