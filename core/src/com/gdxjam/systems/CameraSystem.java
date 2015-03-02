package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
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
	
	private float minZoom;
	private float maxZoom;
	
	private float panScalar = 0.25f;
	private float zoomScalar = 0.25f;

	private Rectangle worldBounds;
	private boolean clampToBounds = true;

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
		maxZoom = (1.0f / worldWidth) * camera.viewportWidth;
		minZoom = worldWidth / camera.viewportWidth;
		updateCameras();
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
		amount *= (camera.zoom / maxZoom) * zoomScalar;
		camera.zoom += amount;
		clamp();
		updateCameras();
	}
	
	private void updateCameras(){
		camera.update();
		
		for (Entry<ParalaxLayer> entry : paralaxLayers.entries()) {
			float coeffeciant = entry.value.coeffeciant;
			float zoom = 1.0f + (camera.zoom * coeffeciant);
			float x = ((camera.position.x / worldBounds.width) * camera.viewportWidth * coeffeciant) + (camera.viewportWidth * 0.5f);
			float y = ((camera.position.y / worldBounds.height) * camera.viewportHeight * coeffeciant) + (camera.viewportHeight * 0.5f);
			OrthographicCamera camera = entry.value.getCamera();
			camera.position.set(x, y, 0);
			camera.zoom = zoom;
			camera.update();
			}
	}

	private void clamp(){
		if(clampToBounds){
			camera.zoom = MathUtils.clamp(camera.zoom, maxZoom, minZoom);
			
			float xMin = (camera.viewportWidth * camera.zoom * 0.5f);
			float xMax = worldBounds.width - xMin; 
			float yMin = (camera.viewportHeight * camera.zoom * 0.5f);
			float yMax = worldBounds.height - yMin;
			
			float x = MathUtils.clamp(camera.position.x, xMin, xMax);
			float y = MathUtils.clamp(camera.position.y, yMin, yMax);
			
			camera.position.set(x, y, 0);
		}
	}

	public void translate(float deltaX, float deltaY) {
		deltaX *= (camera.zoom / maxZoom) * panScalar;
		deltaY *= (camera.zoom / maxZoom) * panScalar;
		camera.translate(deltaX, deltaY);
		clamp();
		updateCameras();
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
