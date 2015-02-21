package com.gdxjam.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.gdxjam.Assets;
import com.gdxjam.EntityManager;
import com.gdxjam.GameWorld;
import com.gdxjam.ai.Squad;
import com.gdxjam.ai.states.UnitState;
import com.gdxjam.components.Components;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.input.DefaultInputProcessor;
import com.gdxjam.input.DesktopGestureListener;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.EntityRenderSystem;
import com.gdxjam.systems.GameWorldSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.ResourceSystem;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.systems.StateMachineSystem;
import com.gdxjam.systems.SteeringSystem;
import com.gdxjam.ui.GameTimeTable;
import com.gdxjam.utils.EntityFactory;

public class SquadFormationTestScreen extends AbstractScreen{
	
	private Stage stage;
	private Label foodLabel;
	private GameTimeTable gameTimeTable;
	private GameWorld world;

	@Override
	public void show () {
		super.show();
		
		stage = new Stage();
		initGUI();
		
		EntityManager.getInstance().initSystems();
		world = createTestWorld();
		EntityManager.getInstance().loadWorld(world);
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(new DefaultInputProcessor());
		multiplexer.addProcessor(new DesktopInputProcessor(EntityManager.getInstance()));
		multiplexer.addProcessor(new GestureDetector(new DesktopGestureListener(EntityManager.getInstance())));
		Gdx.input.setInputProcessor(multiplexer);
	}
	
	public GameWorld createTestWorld () {
		GameWorld world = new GameWorld(64, 36);
		int unitCount = 250;
		for(int i = 0; i < unitCount; i++){
			Entity entity = EntityFactory.createUnit(new Vector2(MathUtils.random(0, world.width), MathUtils.random(0, world.height)));
			Components.STATE_MACHINE.get(entity).stateMachine.changeState(UnitState.IDLE);
		}
		return world;
	}

	
	public void initGUI(){
		Skin skin = Assets.getManager().get(Assets.SKIN, Skin.class);
		foodLabel = new Label("Food: 0 / 0", skin);
		
		gameTimeTable = new GameTimeTable(skin);
		
		TextButton formSquadButton = new TextButton("Form Squad", skin);
		formSquadButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				Squad squad = null;
				SquadSystem squadSystem = EntityManager.getInstance().getSystem(SquadSystem.class);
				
				for(Entity entity : EntityManager.getInstance().getEntitiesFor(Family.all(SteerableBodyComponent.class).get())){
					if(squad == null){
						squad = squadSystem.createSquad(entity);
					}
					else{
						squadSystem.addSquadMember(entity, squad);
					}
				}
			}
		});
		
		TextButton killButton = new TextButton("Kill all", skin);
		killButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				for(Entity entity: EntityManager.getInstance().getEntitiesFor(Family.all(HealthComponent.class).get())){
					Components.HEALTH.get(entity).value = 0;
				}
				
			}
		});
		
		
		Table table = new Table();
		table.setFillParent(true);
		table.defaults().pad(10);
		table.add(formSquadButton);
		table.add(killButton);
		table.add(foodLabel);
		table.add(gameTimeTable);
	
		
		stage.addActor(table);
		table.top().right();
	}
	
	public void updateGUI(GameWorld world){
		foodLabel.setText("Food: " + world.food + " / " + ResourceSystem.foodThreshold);
		gameTimeTable.update();
	}

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
		EntityManager.getInstance().getSystem(CameraSystem.class).getViewport().update(width, height);
	}

	@Override
	public void render (float delta) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		
		EntityManager.getInstance().update(delta);
		updateGUI(world);

		stage.act();
		stage.draw();
	}
	
	@Override
	public void pause () {
		super.pause();
		
//		for (Map map : maps) {
//			map.save(map.getKey());
//		}
	}
}
