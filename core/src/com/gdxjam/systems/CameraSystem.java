package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gdxjam.utils.CameraController;

public class CameraSystem extends EntitySystem{
	
	private OrthographicCamera camera;
    public CameraController cameraController;
	
	public CameraSystem(float viewportWidth, float viewportHeight, CameraController cameraController){
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
        cameraController = new CameraController(camera);
	}
	
	public CameraSystem(float viewportWidth, float viewportHeight){
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
      cameraController = new CameraController(camera);
	}
	
	public CameraSystem(OrthographicCamera camera){
		this.camera = camera;
        this.cameraController = new CameraController(camera);
	}
	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		camera.update();
        if(cameraController != null) cameraController.update(deltaTime);
	}
	
	public OrthographicCamera getCamera(){
		return camera;
	}

}
