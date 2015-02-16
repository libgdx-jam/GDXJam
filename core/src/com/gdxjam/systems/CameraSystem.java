package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraSystem extends EntitySystem{
	
	private OrthographicCamera camera;
	
	public CameraSystem(float viewportWidth, float viewportHeight){
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
	}
	
	public CameraSystem(OrthographicCamera camera){
		this.camera = camera;
	}
	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		camera.update();
	}
	
	public OrthographicCamera getCamera(){
		return camera;
	}

}
