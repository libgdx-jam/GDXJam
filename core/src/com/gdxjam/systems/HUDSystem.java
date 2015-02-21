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
import com.gdxjam.input.Keybinds;
import com.gdxjam.utils.Constants;

public class HUDSystem extends EntitySystem implements Disposable{
	
	private Stage stage;
	private Table hotkeyTable;
	private Array<TextButton> squadButtons;
	
	private Skin skin;
	private TextButtonStyle hotkeyStyle;
	
	public HUDSystem (Skin skin) {
		this.stage = new Stage();
		this.skin = skin;
		this.squadButtons = new Array<TextButton>(true, Constants.maxSquads);
		
		initSkin();
		initGUI();
	}
	
	public void initSkin(){
		NinePatchDrawable draw = new NinePatchDrawable(
			Assets.getInstance().hotkey.button);
		
		hotkeyStyle = new ImageTextButtonStyle();
		hotkeyStyle.up = draw;
		hotkeyStyle.down = draw.tint(Color.GREEN);
		hotkeyStyle.checked = draw;
		hotkeyStyle.font = Assets.getInstance().fonts.medium;
	}
	
	public void initGUI(){
		Table bottomTable = new Table();
		bottomTable.setFillParent(true);
		bottomTable.bottom();
		
		hotkeyTable = new Table();
		
		bottomTable.add(hotkeyTable);
		stage.addActor(bottomTable);
		
		Table actionTableContainer = new Table();
		actionTableContainer.setFillParent(true);
		actionTableContainer.bottom().left();
		actionTableContainer.add(createActionTable());
		stage.addActor(actionTableContainer);
	}
	
	public Table createActionTable(){
		Table actionTable = new Table();
		TextButton actionButon0 = new TextButton("Q", hotkeyStyle);
		TextButton actionButon1 = new TextButton("W", hotkeyStyle);
		TextButton actionButon2 = new TextButton("E", hotkeyStyle);
		TextButton actionButon3 = new TextButton("R", hotkeyStyle);
		TextButton actionButon4 = new TextButton("T", hotkeyStyle);
		actionTable.add(actionButon0);
		actionTable.add(actionButon1);
		actionTable.add(actionButon2);
		actionTable.add(actionButon3);
		actionTable.add(actionButon4);
		return actionTable;
	}
	
	public void packHotkeyTable(){
		hotkeyTable.reset();
		for(TextButton button : squadButtons){
			hotkeyTable.add(button);
		}
	}
	
	public void addSquad(Squad squad){
		TextButton btn = new TextButton(squad.index + 1 + "", hotkeyStyle);
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
