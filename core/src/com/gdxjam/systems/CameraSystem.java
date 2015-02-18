package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraSystem extends EntitySystem{

	private OrthographicCamera camera;
	private Viewport viewport;
	
	public CameraSystem(float viewportWidth, float viewportHeight){
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
		viewport = new ScalingViewport(Scaling.stretch, viewportWidth, viewportHeight, camera);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		camera.update();
	}
	
	public OrthographicCamera getCamera(){
		return (OrthographicCamera)viewport.getCamera();
	}
	
	public Viewport getViewport(){
		return viewport;
	}

}
