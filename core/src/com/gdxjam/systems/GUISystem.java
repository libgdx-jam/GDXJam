
package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.gdxjam.Assets;
import com.gdxjam.ai.state.Messages;
import com.gdxjam.components.SquadComponent.FormationPatternType;
import com.gdxjam.ui.CommandCardContainer;
import com.gdxjam.ui.WaveTimerTable;

public class GUISystem extends EntitySystem implements Telegraph, Disposable {

	private Stage stage;
	private Skin skin;

	private IntMap<Entity> squads = new IntMap<Entity>();

	private CommandCardContainer commandCardContainer;
	private WaveTimerTable waveTimerTable;
	private Label resourceLabel;
	
	private InputSystem inputSystem;

	public GUISystem () {
		this.stage = new Stage();
		this.skin = Assets.skin;
		MessageManager.getInstance().addListener(this, Messages.SQUAD_SELECTED);
		initGUI();
	}
	
	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		this.inputSystem = engine.getSystem(InputSystem.class);
		
		commandCardContainer = new CommandCardContainer(inputSystem, skin, stage);
		Table squadManagmentContainer = new Table();
		squadManagmentContainer.setFillParent(true);
		squadManagmentContainer.add(commandCardContainer).padTop(30);
		squadManagmentContainer.center().bottom();
		stage.addActor(squadManagmentContainer);
	}

	public void initGUI () {
		/** Resource Table */
		resourceLabel = new Label("Resources: XXX", skin);
		Table resourceTable = new Table();
		resourceTable.add(resourceLabel);
		resourceTable.center();

		waveTimerTable = new WaveTimerTable(skin);
		waveTimerTable.right();

		Table rightTable = new Table();
		rightTable.setFillParent(true);
		rightTable.add(waveTimerTable).pad(5);
		rightTable.top().right();

		Table centerTable = new Table();
		centerTable.setFillParent(true);
		centerTable.defaults().pad(5);
		centerTable.add(resourceTable);
		centerTable.top();

		stage.addActor(centerTable);
		stage.addActor(rightTable);

	}



	public void addSquad (Entity squad, int index) {
		commandCardContainer.addSquad(squad, index);
	}

	public void removeSquad (Entity squad) {
		for (Entry<Entity> entry : squads) {
			if (entry.value == squad) {
				squads.remove(entry.key);
				commandCardContainer.removeSquad(squad, entry.key);
				return;
			}
		}

	}

	public void updateSquad (Entity squad) {
		for (Entry<Entity> entry : squads) {
			if (entry.value == squad) {
				commandCardContainer.updateSquadTable(entry.key);
			}
		}
	}
	
	public void setSelected(int index, boolean selected){
		commandCardContainer.setSelected(index, selected);
	}

	public void resize (int screenWidth, int screenHeight) {
		stage.getViewport().update(screenWidth, screenHeight);
	}

	public void updateWaveTime (float timeRemaining) {
		waveTimerTable.update(timeRemaining);
	}

	public void updateResource (int amount) {
		resourceLabel.setText("Resources: " + amount);
	}
	
	public void updateFormationPattern(int index, FormationPatternType pattern){
		commandCardContainer.updateFormationPattern(index, pattern);
	}

	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);

		stage.act();
		stage.draw();
	}

	public Stage getStage () {
		return stage;
	}

	@Override
	public void dispose () {
		stage.dispose();
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		switch(msg.message){
		case Messages.SQUAD_SELECTED:
			int index = (Integer)msg.extraInfo;
			setSelected(index, true);
			return true;
			
		default:
			return false;
		}
	}

}
