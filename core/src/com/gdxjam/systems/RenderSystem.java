package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdxjam.components.Components;
import com.gdxjam.components.VisualComponent;

public class RenderSystem extends IteratingSystem {

	private SpriteBatch batch;
	private OrthographicCamera camera;

	public RenderSystem() {
		super(Family.all(VisualComponent.class).get());
		
		batch = new SpriteBatch();
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.camera = engine.getSystem(CameraSystem.class).getCamera();
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		VisualComponent visual = Components.VISUAL.get(entity);
		//FIXME remobed 
		batch.draw(visual.region, 0, 0, 1, 1, 1, 1, 1, 1,visual.rotation);
	};

	@Override
	public void update(float deltaTime) {
		camera.update();
		
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		
		super.update(deltaTime);
		batch.end();
	}
}