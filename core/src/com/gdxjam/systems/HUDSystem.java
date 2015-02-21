package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gdxjam.Assets;
import com.gdxjam.ai.Squad;
import com.gdxjam.utils.Constants;

public class HUDSystem extends EntitySystem implements Disposable{
	
	private Stage stage;
	private Skin skin;
	
	private Table hotkeyTable;
	private Array<TextButton> squadButtons;
	
	public HUDSystem (Skin skin) {
		this.stage = new Stage();
		this.skin = skin;
		this.squadButtons = new Array<TextButton>(true, Constants.maxSquads);
		
		initGUI();
	}
	
	public void initGUI(){
		Table bottomTable = new Table();
		bottomTable.setFillParent(true);
		bottomTable.bottom();
		
		hotkeyTable = new Table();
		
		bottomTable.add(hotkeyTable);
		stage.addActor(bottomTable);
	}
	
	public void packHotkeyTable(){
		hotkeyTable.reset();
		for(TextButton button : squadButtons){
			hotkeyTable.add(button);
		}
	}
	
	public void addSquad(Squad squad){
		NinePatchDrawable draw = new NinePatchDrawable(
			Assets.getInstance().hotkey.button);
		
		TextButtonStyle style = new ImageTextButtonStyle();
		style.up = draw;
		style.down = draw.tint(Color.GREEN);
		style.checked = draw;
		style.font = Assets.getInstance().fonts.medium;

		TextButton btn = new TextButton(squad.index + 1 + "", style);
		btn.setColor(Color.DARK_GRAY);
		squadButtons.insert(squad.index, btn);
		
		packHotkeyTable();
	}
	
	public void setSelected(Squad squad){
		TextButton button = squadButtons.get(squad.index);
		Color tint = squad.selected ? Color.WHITE : Color.DARK_GRAY;
		button.setColor(tint);
	}
	
	public void resize(int screenWidth, int screenHeight){
		stage.getViewport().update(screenWidth, screenHeight);
	}
	
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose () {
		stage.dispose();
	}

}
