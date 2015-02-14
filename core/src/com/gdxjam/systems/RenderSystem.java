package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdxjam.components.Components;
import com.gdxjam.components.PositionComponent;
import com.gdxjam.components.VisualComponent;

public class RenderSystem extends IteratingSystem {

	private SpriteBatch batch;
	private OrthographicCamera camera;

	public RenderSystem(OrthographicCamera camera) {
		super(Family.all(PositionComponent.class, VisualComponent.class).get());
		
		batch = new SpriteBatch();
		this.camera = camera;
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		
	}

	@Override
	public void removedFromEngine(Engine engine) {

	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PositionComponent position = Components.POSITION.get(entity);
		VisualComponent visual = Components.VISUAL.get(entity);
		
		batch.draw(visual.region, position.x, position.y, 1, 1, 1, 1, 1, 1,
				visual.rotation);
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