
package com.gdxjam.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.gdxjam.components.Components;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.ParalaxComponent;
import com.gdxjam.components.PhysicsComponent;
import com.gdxjam.components.SpriteComponent;

public class EntityRenderSystem extends SortedIteratingSystem implements Disposable {
	private static final String TAG = "[" + EntityRenderSystem.class.getSimpleName() + "]";
	private static final int spriteRotationOffset = -0;
	private static final float healthBarHeight = 0.15f;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private CameraSystem cameraSystem;
	private int currentLayer = -10;
	
	
	
	public EntityRenderSystem () {
		super(Family.all(SpriteComponent.class).get(), new Comparator<Entity>() {

			@Override
			public int compare (Entity e1, Entity e2) {
				if (Components.PARALAX.has(e1) && Components.PARALAX.has(e2)) {
					ParalaxComponent p1 = Components.PARALAX.get(e1);
					ParalaxComponent p2 = Components.PARALAX.get(e2);
					return p1.layer < p2.layer ? 1 : 0;
				} else if (Components.PARALAX.has(e1) || Components.PARALAX.has(e2)) {
					return Components.PARALAX.has(e1) ? -1 : 1;
				} else {
					return 0;
				}

			}
		});

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
		cameraSystem = engine.getSystem(CameraSystem.class);
	}

	@Override
	public void update (float deltaTime) {
		currentLayer = 0;
		batch.setProjectionMatrix(cameraSystem.getParalaxCamera(currentLayer).combined);
		batch.begin();
		shapeRenderer.setProjectionMatrix(cameraSystem.getCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);
		super.update(deltaTime);
		batch.end();
		shapeRenderer.end();
	}

	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		Sprite sprite = Components.SPRITE.get(entity).sprite;

		if (Components.PARALAX.has(entity)) {
			ParalaxComponent paralaxComp = Components.PARALAX.get(entity);
			if (currentLayer != paralaxComp.layer) {
				batch.setProjectionMatrix(cameraSystem.getParalaxCamera(paralaxComp.layer).combined);
			}

		} else {
			if (currentLayer != -1) {
				batch.setProjectionMatrix(cameraSystem.getCamera().combined);
				currentLayer = -1;
			}
			if (Components.PHYSICS.has(entity)) {
				PhysicsComponent physics = Components.PHYSICS.get(entity);
				Vector2 pos = physics.body.getPosition();
				sprite.setCenter(pos.x, pos.y);
				sprite.setRotation((MathUtils.radiansToDegrees * physics.body.getAngle()) + spriteRotationOffset);
			}
		}

		sprite.draw(batch);
		
		//NOTE: If an entity has health but no sprite this will not get drawn
		if(Components.HEALTH.has(entity)){
			HealthComponent healthComp = Components.HEALTH.get(entity);
			if(healthComp.value < healthComp.max){
				float percent = (float)healthComp.value / (float)healthComp.max;
				shapeRenderer.setColor(Color.RED);
				shapeRenderer.rect(sprite.getX(), sprite.getY() + sprite.getHeight(), sprite.getWidth(), healthBarHeight);
				shapeRenderer.setColor(Color.GREEN);
				shapeRenderer.rect(sprite.getX(), sprite.getY() + sprite.getHeight(), sprite.getWidth() * percent, healthBarHeight);
			}
		}
		
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

}
