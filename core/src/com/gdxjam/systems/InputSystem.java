
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;
import com.gdxjam.components.SquadComponent.FormationPatternType;
import com.gdxjam.ecs.Components;
import com.gdxjam.input.Keybinds;
import com.gdxjam.ui.dialog.PauseDialog;
import com.gdxjam.utils.Constants;

public class InputSystem extends EntitySystem implements InputProcessor {

	private static final String TAG = InputSystem.class.getSimpleName();
	private static final float CAMERA_ZOOM_SCALAR = 0.1f;

	private IntMap<Entity> squadIndices = new IntMap<Entity>();
	private Array<Integer> selectedIndices = new Array<Integer>();
	private IntMap<Integer> keybindIndices = new IntMap<Integer>();

	private CameraSystem cameraSystem;

	private InputMultiplexer multiplexer;
	private GUISystem guiSystem;

	public InputSystem (GUISystem guiSystem) {
		this.guiSystem = guiSystem;

	}

	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		cameraSystem = engine.getSystem(CameraSystem.class);
		initalizeInput();
	}

	public InputMultiplexer getMultiplexer () {
		return multiplexer;
	}

	public void initalizeInput () {
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(guiSystem.getStage());
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);

		keybindIndices.put(Keybinds.SQUAD0, 0);
		keybindIndices.put(Keybinds.SQUAD1, 1);
		keybindIndices.put(Keybinds.SQUAD2, 2);
		keybindIndices.put(Keybinds.SQUAD3, 3);
		keybindIndices.put(Keybinds.SQUAD4, 4);

		keybindIndices.put(Keybinds.FORMATION0, 0);
		keybindIndices.put(Keybinds.FORMATION1, 1);
		keybindIndices.put(Keybinds.FORMATION2, 2);
		keybindIndices.put(Keybinds.FORMATION3, 3);
		keybindIndices.put(Keybinds.FORMATION4, 4);
		keybindIndices.put(Keybinds.FORMATION5, 5);
	}

	public void swapSquadSlot (int a, int b) {
		Entity squadA = squadIndices.get(a);
		Entity squadB = squadIndices.get(b);
		squadIndices.remove(a);
		squadIndices.remove(b);

		if (squadB != null) squadIndices.put(a, squadB);
		if (squadA != null) squadIndices.put(b, squadA);

		if (selectedIndices.contains(a, true)) {
			setSelected(a, false);
			setSelected(b, true);
		}

		if (selectedIndices.contains(b, true)) {
			setSelected(b, false);
			setSelected(a, true);
		}
	}

	public void clearSelected () {
		for (int i : selectedIndices) {
			setSelected(i, false);
		}
	}

	public boolean addSquad (Entity squad) {
		for (int i = 0; i < Constants.maxSquads; i++) {
			if (!squadIndices.containsKey(i)) {
				return addSquad(squad, i);
			}
		}
		return false;
	}

	public boolean addSquad (Entity squad, int index) {
		squadIndices.put(index, squad);
		guiSystem.addSquad(squad, index);
		return true;
	}

	public boolean removeSquad (Entity squad) {
		int index = squadIndices.findKey(squad, true, -1);

		if (index >= 0) {
			squadIndices.remove(index);
			selectedIndices.removeValue(index, true);

			guiSystem.removeSquad(squad, index);
			return true;
		}
		return false;
	}

	public void setSelected (int index, boolean selected) {
		if (!squadIndices.containsKey(index)) {
			if (selectedIndices.contains(index, true)) selectedIndices.removeValue(index, true);
			return;
		}

		if (selected)
			selectedIndices.add(index);
		else
			selectedIndices.removeValue(index, true);

		guiSystem.setSelected(index, selected);
	}

	public void setFormationPattern (FormationPatternType pattern) {
		for (int index : selectedIndices) {
			Entity squad = squadIndices.get(index);
			Components.SQUAD.get(squad).setFormationPattern(pattern);
			guiSystem.updateFormationPattern(index, pattern);
		}
	}

	public void setTargetForSelected (Vector2 target) {
		for (int i : selectedIndices) {
			Entity squad = squadIndices.get(i);
			Components.SQUAD.get(squad).setTarget(target);
		}
	}

	@Override
	public boolean keyDown (int keycode) {
		boolean clearSelection = false;
		boolean appendSelection = true;

		if (Gdx.input.isKeyPressed(Keybinds.SELECTION_DECREASE))
			appendSelection = false;
		else if (!Gdx.input.isKeyPressed(Keybinds.SELECTION_INCREASE)) clearSelection = true;

		switch (keycode) {
		// Keybindings for selecting squads
		case Keybinds.SQUAD0:
		case Keybinds.SQUAD1:
		case Keybinds.SQUAD2:
		case Keybinds.SQUAD3:
		case Keybinds.SQUAD4:
			if (clearSelection) clearSelected();
			setSelected(keybindIndices.get(keycode), appendSelection);
			return true;

		// Keybindings for selecting formation patterns
		case Keybinds.FORMATION0:
		case Keybinds.FORMATION1:
		case Keybinds.FORMATION2:
		case Keybinds.FORMATION3:
		case Keybinds.FORMATION4:
		case Keybinds.FORMATION5:
			FormationPatternType pattern = FormationPatternType.values()[keybindIndices.get(keycode)];
			setFormationPattern(pattern);
			return true;

		case Keybinds.TATICS_TOGGLE:
			// TODO INPUT: toggle the tatical state of the squad
			return true;

		case Keys.SPACE:
			// TODO INPUT: move camera to squad
			return true;

		case Keys.F12:
			if (Gdx.app.getType() == ApplicationType.Desktop) {
				// NOTE: Comment this out to run GWT
				// ScreenshotFactory.saveScreenshot();
			}
			return true;
		case Keys.ESCAPE:
			GameManager.pause();
			PauseDialog dialog = new PauseDialog(Assets.skin);
			dialog.show(guiSystem.getStage());
			return true;

		}

		return false;
	}
	
	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		switch (button) {
		case Buttons.LEFT:
			Vector2 position = cameraSystem.screenToWorldCords(screenX, screenY);
			setTargetForSelected(position);
			return true;
		}
		return false;
	}


	@Override
	public boolean keyUp (int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		cameraSystem.zoom(amount * CAMERA_ZOOM_SCALAR);
		return false;
	}
	
	

}
