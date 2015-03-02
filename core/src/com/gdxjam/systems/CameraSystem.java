package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraSystem extends EntitySystem {

	private OrthographicCamera camera;
	private IntMap<ParalaxLayer> paralaxLayers = new IntMap<CameraSystem.ParalaxLayer>();
	private Viewport viewport;
	private Vector2 target;
	boolean smooth = false;

	private Rectangle worldBounds;
	private boolean dontExceedBounds = true;

	public CameraSystem(float viewportWidth, float viewportHeight) {
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
		viewport = new ScalingViewport(Scaling.stretch, viewportWidth,
				viewportHeight, camera);

		addParalaxLayer(0, 0.0f);
		addParalaxLayer(1, 0.10f);
		addParalaxLayer(2, 0.50f);
	}

	public void setWorldBounds(float worldWidth, float worldHeight) {
		worldBounds = new Rectangle(0, 0, worldWidth, worldHeight);
	}

	public void addParalaxLayer(int layerIndex, float paralaxCoeffeciant) {
		ParalaxLayer layer = new ParalaxLayer(layerIndex, paralaxCoeffeciant,
				camera.viewportWidth, camera.viewportHeight);
		paralaxLayers.put(layerIndex, layer);
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (smooth && target != null) {
			camera.position.add(camera.position.cpy().scl(-1)
					.add(target.x, target.y, 0).scl(0.04f));
		}
		camera.update();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public OrthographicCamera getParalaxCamera(int layer) {
		return paralaxLayers.get(layer).getCamera();
	}

	public Viewport getViewport() {
		return viewport;
	}

	public Vector2 screenToWorldCords(float screenX, float screenY) {
		Vector3 pos = new Vector3(screenX, screenY, 0);
		pos.set(camera.unproject(pos));
		return new Vector2(pos.x, pos.y);
	}

	// Camera Controller Methods
	public void zoom(float amount) {
		camera.zoom += amount;
		for (Entry<ParalaxLayer> entry : paralaxLayers.entries()) {
			float coeffeciant = entry.value.coeffeciant;
			entry.value.getCamera().zoom += (amount * coeffeciant);
			entry.value.getCamera().update();
		}
	}

	public void translate(float deltaX, float deltaY) {
		Vector2 camPos = new Vector2(camera.position.x + deltaX,
				camera.position.y + deltaY);
		Rectangle camBounds = new Rectangle(camPos.x
				- (camera.viewportWidth * camera.zoom * 0.5f), camPos.y
				- (camera.viewportHeight * camera.zoom * 0.5f),
				camera.viewportWidth * camera.zoom, camera.viewportHeight
						* camera.zoom);
		if (worldBounds.contains(camBounds)) {
			camera.translate(deltaX, deltaY);
			for (Entry<ParalaxLayer> entry : paralaxLayers.entries()) {
				float coeffeciant = entry.value.coeffeciant;
				entry.value.getCamera().translate(deltaX * coeffeciant,
						deltaY * coeffeciant);
				entry.value.getCamera().update();
			}
		}
	}

	public void setTarget(Vector2 target) {
		this.target = target;
	}

	public void smoothFollow(Vector2 target) {
		smooth = true;
		this.target = target;
	}

	public void goTo(float posX, float posY) {
		target = null;
		camera.position.set(posX, posY, 0);
	}

	public void goToSmooth(Vector2 position) {
		target = position.cpy();
		smooth = true;
	}

	public class ParalaxLayer {

		private int index;
		private float coeffeciant;
		private OrthographicCamera camera;

		public ParalaxLayer(int index, float coeffeciant, float viewportWidth,
				float viewportHeight) {
			this.index = index;
			this.coeffeciant = coeffeciant;

			camera = new OrthographicCamera(viewportWidth, viewportHeight);
			camera.position.set(viewportWidth * 0.5f, viewportHeight * 0.5f, 0);
			camera.update();
		}

		public OrthographicCamera getCamera() {
			return camera;
		}
	}

}
