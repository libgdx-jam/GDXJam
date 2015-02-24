package com.gdxjam.screens;

import com.badlogic.gdx.math.Vector2;
import com.gdxjam.EntityManager;
import com.gdxjam.GameManager;
import com.gdxjam.utils.EntityFactory;


public class GameScreen extends AbstractScreen {
	
	private EntityManager engine;
	
	@Override
	public void show () {
		super.show();
		
		engine = GameManager.initEngine();
		createWorld();
	}
	
	public void createWorld(){
		EntityFactory.createOutpost(new Vector2(32, 18));
	}
	
	@Override
	public void render (float delta) {
		super.render(delta);
		
		engine.update(delta);
	}

}
