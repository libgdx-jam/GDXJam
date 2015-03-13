package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.gdxjam.ai.state.Messages;
import com.gdxjam.ecs.Components;
import com.gdxjam.input.Keybinds;
import com.gdxjam.utils.Constants;

public class InputSystem extends EntitySystem implements InputProcessor{
	
	public Array<Entity> selectedSquads = new Array<Entity>();
	public IntMap<Entity> squadSlot = new IntMap<Entity>();
	public IntMap<Integer> keybindIndices = new IntMap<Integer>();

	private CameraSystem cameraSystem;
	
	private InputMultiplexer multiplexer;
	
	public InputSystem(GUISystem guiSystem){
		initalizeInput();
	}
	
	public void initalizeInput(){
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
		
		keybindIndices.put(Keybinds.SQUAD0, 0);
		keybindIndices.put(Keybinds.SQUAD1, 1);
		keybindIndices.put(Keybinds.SQUAD2, 2);
		keybindIndices.put(Keybinds.SQUAD3, 3);
		keybindIndices.put(Keybinds.SQUAD4, 4);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		cameraSystem = engine.getSystem(CameraSystem.class);
	}
	
	public InputMultiplexer getMultiplexer(){
		return multiplexer;
	}
	
	public void swapSquadSlot(int a, int b){
		Entity squadA = squadSlot.get(a);
		Entity squadB = squadSlot.get(b);
	
		squadSlot.put(a, squadB);
		squadSlot.put(b, squadA);
	}
	
	public void clearSelected(){
		selectedSquads.clear();
	}
	
	public void registerSquad(int index, Entity squad){
		squadSlot.put(index, squad);
	}
	
	public void setSelected(int index, boolean selected){
		if(!squadSlot.containsKey(index)) return;
		
		Entity squad = squadSlot.get(index);
		
		if(selected){
			selectedSquads.add(squad);
			MessageManager.getInstance().dispatchMessage(Messages.SQUAD_SELECTED, squad);
		}
		else
			selectedSquads.removeValue(squad, true);

	}
	
	public void setTargetForSelected(Vector2 target){
		//Sets the target of all the selected squads
		for(Entity squad : selectedSquads){
			Components.SQUAD.get(squad).setTarget(target);
		}
	}


	@Override
	public boolean keyDown(int keycode) {
		boolean appendSelection = true;
		
		if(Gdx.input.isKeyPressed(Keys.ALT_LEFT))
			appendSelection = false;
		else if(!Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT))
			appendSelection = false;

		switch (keycode) {
		case Keybinds.SQUAD0:
		case Keybinds.SQUAD1:
		case Keybinds.SQUAD2:
		case Keybinds.SQUAD3:
		case Keybinds.SQUAD4:
			if(!appendSelection)
				clearSelected();
			
			setSelected(keybindIndices.get(keycode), appendSelection);
			return true;

			// return true;
		case Keybinds.ACTION3:

			return true;
		case Keybinds.ACTION4:
			return true;

		case Keys.SPACE:
			return true;
		case Keys.F12:
			if (Gdx.app.getType() == ApplicationType.Desktop) {
				// NOTE: Comment this out to run GWT
				// ScreenshotFactory.saveScreenshot();
			}
			return true;
		case Keys.ESCAPE:
			Constants.isPaused = !Constants.isPaused;
			return true;

		}

		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		switch(button){
		case Buttons.LEFT:
			Vector2 position = cameraSystem.screenToWorldCords(screenX, screenY);
			setTargetForSelected(position);
			return true;
		}
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
