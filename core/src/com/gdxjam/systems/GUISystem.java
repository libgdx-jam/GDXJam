
package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.gdxjam.Assets;
import com.gdxjam.ai.state.TelegramMessage;
import com.gdxjam.components.SquadComponent.FormationPatternType;
import com.gdxjam.ui.CommandCardContainer;
import com.gdxjam.ui.WaveTimerTable;

public class GUISystem extends EntitySystem implements Telegraph, Disposable {

	private Stage stage;
	private Skin skin;

	private CommandCardContainer commandCardContainer;
	private WaveTimerTable waveTimerTable;
	private Label resourceLabel;

	private InputSystem inputSystem;
	
	private boolean isResourceAlertActive = false;
	private Task resourceAlertTask;
	
	private static final Color DEFAULT_COLOR = Color.WHITE;
	private static final Color ALERT_COLOR = Color.RED;

	public GUISystem () {
		this.stage = new Stage();
		this.skin = Assets.skin;
		
		MessageManager.getInstance().addListener(this, TelegramMessage.SQUAD_INPUT_SELECTED.ordinal());
		MessageManager.getInstance().addListener(this, TelegramMessage.GUI_INSUFFICIENT_RESOURCES.ordinal());
		
		initGUI();
		initAlertTasks();
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

	private void initGUI () {
		/** Resource Table */
		resourceLabel = new Label("Resources: XXX", skin);
		resourceLabel.setColor(DEFAULT_COLOR);
		
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
	
	private void initAlertTasks(){
		resourceAlertTask = new Task() {
			
			@Override
			public void run () {
				Color color = ALERT_COLOR;
				if(resourceLabel.getColor().equals(ALERT_COLOR))
					color = DEFAULT_COLOR;
				
				resourceLabel.setColor(color);
			}
		};
	}

	public void addSquad (Entity squad, int index) {
		commandCardContainer.addSquad(squad, index);
	}

	public void removeSquad (Entity squad, int index) {
		commandCardContainer.removeSquad(squad, index);
	}

	public void updateSquad (Entity squad) {

	}

	public void setSelected (int index, boolean selected) {
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

	public void updateFormationPattern (int index, FormationPatternType pattern) {
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
	public boolean handleMessage (Telegram msg) {
		TelegramMessage telegramMsg = TelegramMessage.values()[msg.message];
		switch (telegramMsg) {
		
		case SQUAD_INPUT_SELECTED:
			int index = (Integer)msg.extraInfo;
			setSelected(index, true);
			return true;
			
		case GUI_INSUFFICIENT_RESOURCES:
			if(resourceAlertTask.isScheduled())
				resourceAlertTask.cancel();
			Timer.schedule(resourceAlertTask, 0.0f, 0.15f, 5);
			return true;

		default:
			return false;
		}
	}

}
