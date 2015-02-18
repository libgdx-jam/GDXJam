
package com.gdxjam.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gdxjam.Assets;
import com.gdxjam.DesktopInputProcessor;
import com.gdxjam.GameWorld;
import com.gdxjam.ai.Squad;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.EntityRenderSystem;
import com.gdxjam.systems.GameWorldSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.ResourceSystem;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.systems.StateSystem;
import com.gdxjam.systems.SteeringSystem;
import com.gdxjam.utils.EntityFactory;

public class GameScreen extends AbstractScreen {

	private GameWorld world;

	private PooledEngine engine;
	private PhysicsSystem physicsSystem;
	
	private Squad squadA;
	private Squad squadB;
	
	//GUI
	private Label foodLabel;

	@Override
	public void show () {
		super.show();
		
		initEngine();
		world = createTestWorld();
		loadWorld(world);

		initGUI();
		
		DesktopInputProcessor input = new DesktopInputProcessor(engine);
		Gdx.input.setInputProcessor(input);
	}
	
	public GameWorld createTestWorld () {
		GameWorld world = new GameWorld();
		
		//squadA = createSquad(new Vector2(10, 10));
		//squadB = createSquad(new Vector2(0, 10));

		//EntityFactory.createFortress(new Vector2(10, 10), 12, 12);
		return world;
	}
	
//	public Squad createSquad (Vector2 position) {
//		Squad squad = new Squad(position);
//		position.set(position.x - 1, position.y - 1);
//		for (int x = 0; x < 3; x++) {
//			for (int y = 0; y < 3; y++) {
//				if (x == 1 && y == 1) {
//					squad.addMember(EntityFactory.createCommander(new Vector2(position.x + x, position.y + y)));
//				} else {
//					squad.addMember(EntityFactory.createUnit(new Vector2(position.x + x, position.y + y)));
//				}
//			}
//		}
//		return squad;
//	}
	
	public void initGUI(){
		Skin skin = Assets.getManager().get(Assets.SKIN, Skin.class);
		foodLabel = new Label("Food: 0 / 0", skin);
		
		Table table = new Table();
		table.setFillParent(true);
		table.defaults().pad(10);
		table.add(foodLabel);
		
		stage.addActor(table);
		table.top().right();
	}
	
	public void updateGUI(GameWorld world){
		foodLabel.setText("Food: " + world.food + " / " + ResourceSystem.foodThreshold);
	}
	
	public PooledEngine initEngine () {
		engine = new PooledEngine();
		EntityFactory.setEngine(engine);

		CameraSystem cameraSystem = new CameraSystem(64, 36);
		cameraSystem.getCamera().position.set(32, 18, 0);
		engine.addSystem(cameraSystem);
		

		physicsSystem = new PhysicsSystem();
		engine.addSystem(physicsSystem);

		SteeringSystem steeringSystem = new SteeringSystem();
		
		//AI
		engine.addSystem(steeringSystem);
		engine.addSystem(new StateSystem());
		engine.addSystem(new SquadSystem());
		
		engine.addSystem(new EntityRenderSystem(cameraSystem.getCamera()));
		return engine;
	}
	
	public void loadWorld(GameWorld world){
		engine.addSystem(new ResourceSystem(world));
		engine.addSystem(new GameWorldSystem(world));
	}
	

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
		
		engine.getSystem(CameraSystem.class).getViewport().update(width, height);
	}

	@Override
	public void render (float delta) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		
		engine.update(delta);
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
