package com.gdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdxjam.components.OrientationComponent;
import com.gdxjam.components.PositionComponent;
import com.gdxjam.components.VisualComponent;

public class RenderSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;

	private SpriteBatch batch;
	private OrthographicCamera camera;

	private ComponentMapper<PositionComponent> pm = ComponentMapper
			.getFor(PositionComponent.class);
	private ComponentMapper<VisualComponent> vm = ComponentMapper
			.getFor(VisualComponent.class);
    private ComponentMapper<OrientationComponent> om = ComponentMapper
            .getFor(OrientationComponent.class);

	public RenderSystem(OrthographicCamera camera) {
		batch = new SpriteBatch();

		this.camera = camera;
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class,
				VisualComponent.class));
	}

	@Override
	public void removedFromEngine(Engine engine) {

	}

	@Override
	public void update(float deltaTime) {
		PositionComponent position;
		VisualComponent visual;

		camera.update();

		batch.begin();
		batch.setProjectionMatrix(camera.combined);

		for (int i = 0; i < entities.size(); ++i) {
			Entity e = entities.get(i);

			position = pm.get(e);
			visual = vm.get(e);

            if(om.has(e)){
                visual.rotation = om.get(e).getAngleDeg();
            }

			// batch.draw(visual.region, position.x, position.y, 1, 1);
			batch.draw(visual.region, position.pos.x, position.pos.y, 1, 1, 1, 1, 1, 1,
					visual.rotation);
		}

		batch.end();
	}
}