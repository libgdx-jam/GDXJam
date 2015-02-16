
package com.gdxjam.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.BattalionInputTest;
import com.gdxjam.ai.Battalion;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.SpriteRenderSystem;
import com.gdxjam.systems.SteeringSystem;
import com.gdxjam.utils.EntityFactory;

public class TestScreen extends AbstractScreen {

	private static final int PIXELS_PER_UNIT = 32;

	private PooledEngine engine;
	private PhysicsSystem physicsSystem;

	private Battalion battalionA;
	private Battalion battalionB;

	@Override
	public void show () {
		initEngine();
		createTestWorld();

		BattalionInputTest input = new BattalionInputTest(engine.getSystem(CameraSystem.class).getCamera(), battalionA, battalionB);
		Gdx.input.setInputProcessor(input);
	}

	public void createTestWorld () {
		Vector2 posA = new Vector2(10, 10);
		battalionA = new Battalion(posA);
		createSquad(posA, battalionA);

		Vector2 posB = new Vector2(0, 10);
		battalionB = new Battalion(posB);
		createSquad(posB, battalionB);
		
		EntityFactory.createFortress(new Vector2(10, 10), 12, 12);
	}

	public void createSquad (Vector2 position, Battalion battalion) {
		position.set(position.x - 1, position.y - 1);
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (x == 1 && y == 1) {
					battalion.addMember(EntityFactory.createCommander(new Vector2(position.x + x, position.y + y)));
				} else {
					battalion.addMember(EntityFactory.createUnit(new Vector2(position.x + x, position.y + y)));
				}
			}
		}
	}

	public void initEngine () {
		engine = new PooledEngine();
		EntityFactory.setEngine(engine);
		
		OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth() / PIXELS_PER_UNIT, Gdx.graphics.getHeight()
			/ PIXELS_PER_UNIT);
		camera.position.set(10, 10, 0);
		engine.addSystem(new CameraSystem(camera));

		physicsSystem = new PhysicsSystem();
		engine.addSystem(physicsSystem);

		SteeringSystem steeringSystem = new SteeringSystem();
		engine.addSystem(steeringSystem);
		
		engine.addSystem(new SpriteRenderSystem(camera));
		
		
	}

	@Override
	public void render (float delta) {
		super.render(delta);
		engine.update(delta);
	}

}
