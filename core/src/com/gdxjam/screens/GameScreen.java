
package com.gdxjam.screens;

import java.util.ArrayList;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.DesktopInputProcessor;
import com.gdxjam.ai.Squad;
import com.gdxjam.map.GameMapPixMap;
import com.gdxjam.map.Map;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.EntityRenderSystem;
import com.gdxjam.systems.SteeringSystem;
import com.gdxjam.utils.EntityFactory;

public class GameScreen extends AbstractScreen {

	ArrayList<Map> maps = new ArrayList<Map>();
	GameMapPixMap map;

	private PooledEngine engine;
	private PhysicsSystem physicsSystem;
	
	private Squad squadA;
	private Squad squadB;

	@Override
	public void show () {
		initEngine();
		createTestWorld();

		DesktopInputProcessor input = new DesktopInputProcessor(engine.getSystem(CameraSystem.class).getCamera(), squadA, squadB);
		Gdx.input.setInputProcessor(input);
	}
	
	public void createTestWorld () {
//		map = new GameMapPixMap();
//		map.setKey("test");
//		map.convertPixmap("test.png");
//		map.addToAshley(engine);
		
		squadA = createSquad(new Vector2(10, 10));
		squadB = createSquad(new Vector2(0, 10));

		EntityFactory.createFortress(new Vector2(10, 10), 12, 12);
	}
	
	public Squad createSquad (Vector2 position) {
		Squad squad = new Squad(position);
		position.set(position.x - 1, position.y - 1);
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (x == 1 && y == 1) {
					squad.addMember(EntityFactory.createCommander(new Vector2(position.x + x, position.y + y)));
				} else {
					squad.addMember(EntityFactory.createUnit(new Vector2(position.x + x, position.y + y)));
				}
			}
		}
		return squad;
	}
	
	public void initEngine () {
		engine = new PooledEngine();
		EntityFactory.setEngine(engine);

		CameraSystem cameraSystem = new CameraSystem(64, 36);
		cameraSystem.getCamera().position.set(10, 10, 0);
		engine.addSystem(cameraSystem);
		

		physicsSystem = new PhysicsSystem();
		engine.addSystem(physicsSystem);

		SteeringSystem steeringSystem = new SteeringSystem();
		engine.addSystem(steeringSystem);

		engine.addSystem(new EntityRenderSystem(cameraSystem.getCamera()));

	}
	

	@Override
	public void resize (int width, int height) {
		engine.getSystem(CameraSystem.class).getViewport().update(width, height);
	}

	@Override
	public void render (float delta) {
		super.render(delta);
		engine.update(delta);
	}
	
	@Override
	public void pause () {
		for (Map map : maps) {
			map.save(map.getKey());
		}
	}

}
