package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.gdxjam.components.Components;
import com.gdxjam.components.NinePatchComponent;
import com.gdxjam.components.PhysicsComponent;
import com.gdxjam.components.SpriteComponent;

public class EntityRenderSystem extends IteratingSystem implements Disposable {

	private static final int spriteRotationOffset = -90;

	private SpriteBatch batch;
	private OrthographicCamera camera;

	public EntityRenderSystem(OrthographicCamera camera) {
		super(Family.one(SpriteComponent.class).get());

		this.camera = camera;
		batch = new SpriteBatch();
	}

	@Override
	public void update(float deltaTime) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		super.update(deltaTime);
		batch.end();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if(Components.SPRITE.has(entity)){
			SpriteComponent spriteComp = Components.SPRITE.get(entity);
			
			if(Components.STEERABLE_BODY.has(entity) || Components.PHYSICS.has(entity)){
				PhysicsComponent physics = Components.STEERABLE_BODY.has(entity) ? Components.STEERABLE_BODY.get(entity) : Components.PHYSICS.get(entity);
				Vector2 pos = physics.body.getPosition();
				spriteComp.sprite.setCenter(pos.x, pos.y);
				spriteComp.sprite
						.setRotation((MathUtils.radiansToDegrees * physics.body
								.getAngle()) + spriteRotationOffset);
			}
	
			spriteComp.sprite.draw(batch);
		}
		

	}

	@Override
	public void dispose() {
		batch.dispose();
	}

}
