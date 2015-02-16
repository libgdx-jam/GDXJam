package com.gdxjam.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gdxjam.components.MovementComponent;
import com.gdxjam.components.PositionComponent;

public class UpdateSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;

	private ComponentMapper<PositionComponent> pm = ComponentMapper
			.getFor(PositionComponent.class);
	private ComponentMapper<MovementComponent> mm = ComponentMapper
			.getFor(MovementComponent.class);

	public UpdateSystem() {
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine
				.getEntitiesFor(Family.getFor(MovementComponent.class));
	}

	@Override
	public void removedFromEngine(Engine engine) {

	}

	@Override
	public void update(float deltaTime) {
		PositionComponent position;
		MovementComponent movement;

		for (int i = 0; i < entities.size(); ++i) {
			Entity e = entities.get(i);
			position = pm.get(e);

			System.out.println(position.getX() + " " + position.getY());

			movement = mm.get(e);

			movement.entity.position = position.position;

			movement.entity.update(deltaTime);

			position.position = movement.entity.position;

		}

	}
}