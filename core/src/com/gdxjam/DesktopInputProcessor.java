package com.gdxjam;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.gdxjam.ai.Squad;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.GameWorldSystem;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.utils.ScreenshotFactory;

public class DesktopInputProcessor implements InputProcessor{

	private PooledEngine engine;
	private OrthographicCamera camera;
	private GameWorld world;
	
	public DesktopInputProcessor(PooledEngine engine) {
		this.engine = engine;
		this.camera = engine.getSystem(CameraSystem.class).getCamera();
		this.world = engine.getSystem(GameWorldSystem.class).getWorld();
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		pos.set(camera.unproject(pos));
//		camera.position.set(touch.x, touch.y, 0);
//		camera.update();
		if(button == Buttons.LEFT){
			//battalionA.setTarget(pos.x, pos.y);
		}
		else{
			//battalionB.setTarget(pos.x, pos.y);
		}
		return true;
	}

	@Override
	public boolean scrolled(int amount) {

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.NUM_1:
			world.food++;
			return true;
		case Keys.NUM_2:
			Squad squad = null;
			SquadSystem squadSystem = engine.getSystem(SquadSystem.class);
			for(Entity entity : engine.getEntitiesFor(Family.all(SteerableBodyComponent.class).get())){
				if(squad == null){
					squad = squadSystem.createSquad(entity);
				}
				else{
					squadSystem.addSquadMember(entity, squad);
				}
			}
			
		case Keys.F12:
			ScreenshotFactory.saveScreenshot();
			return true;
		}

		return false;
	}
	
}
