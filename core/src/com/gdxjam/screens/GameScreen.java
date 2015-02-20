package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.EntityManager;
import com.gdxjam.GameWorld;
import com.gdxjam.input.DesktopInputProcessor;
import com.gdxjam.systems.GUISystem;
import com.gdxjam.utils.EntityFactory;

public class GameScreen extends AbstractScreen {

	private GUISystem gui;

	// private Squad squadA;
	// private Squad squadB;

	@Override
	public void show() {
		super.show();

		EntityManager.getInstance().initSystems();
		GameWorld world = createTestWorld();
		EntityManager.getInstance().loadWorld(world);

		initGUI();
		DesktopInputProcessor input = new DesktopInputProcessor(EntityManager.getInstance());
		Gdx.input.setInputProcessor(input);
	}

	public GameWorld createTestWorld() {
		GameWorld world = new GameWorld(64, 36);

//		Squad squadA = EntityFactory.createSquad(new Vector2(0, 10));
//		Squad squadB = EntityFactory.createSquad(new Vector2(10, 10));
//		Squad squadC = EntityFactory.createSquad(new Vector2(0, 0));
//		Squad squadD = EntityFactory.createSquad(new Vector2(10, 0));

		EntityFactory.createFortress(new Vector2(10, 10), 12, 12);
		return world;
	}

	public void initGUI() {
		/**
		 * The gui viewport needs to be set to something consistent
		 * */
		gui = new GUISystem(640, 360, EntityManager.getInstance());
		EntityManager.getInstance().addSystem(gui);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		gui.resize(width, height);
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glClearColor(0, 0, 0, 1);

		EntityManager.getInstance().update(delta);
	}

	@Override
	public void pause() {
		super.pause();
		gui.pause();

		// for (Map map : maps) {
		// map.save(map.getKey());
		// }
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		gui.resume();
	}

}
