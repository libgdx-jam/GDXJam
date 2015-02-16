package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.Components;
import com.gdxjam.components.SpriteComponent;
import com.gdxjam.components.SteerableBodyComponent;

public class SpriteRenderSystem extends IteratingSystem{
	
	private static final int spriteRotationOffset = -90;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	
	public SpriteRenderSystem(OrthographicCamera camera){
		super(Family.all(SpriteComponent.class).get());
		
		this.camera = camera;
		batch = new SpriteBatch();
	}
	
	@Override
	public void update (float deltaTime) {
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		super.update(deltaTime);
		batch.end();
	}
	
	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		SpriteComponent spriteComp = Components.SPRITE.get(entity);
		
		if(Components.STEERABLE_BODY.has(entity)){
			SteerableBodyComponent physics = Components.STEERABLE_BODY.get(entity);
			Vector2 pos = physics.body.getPosition();
			spriteComp.sprite.setCenter(pos.x, pos.y);
			spriteComp.sprite.setRotation((MathUtils.radiansToDegrees * physics.body.getAngle()) + spriteRotationOffset);
		}
		
		spriteComp.sprite.draw(batch);
	}

}
