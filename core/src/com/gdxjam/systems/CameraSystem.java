package com.gdxjam.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraSystem extends EntitySystem{
	
	private OrthographicCamera camera;
	private Vector2 target;
	boolean smooth = false;
	
	public CameraSystem(float viewportWidth, float viewportHeight){
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
	}
	
	public CameraSystem(OrthographicCamera camera){
		this.camera = camera;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
      if (smooth && target != null){
         camera.position.add(camera.position.cpy().scl(-1).add(target.x,target.y,0).scl(0.04f));
     }
		camera.update();
	}
	
	public OrthographicCamera getCamera(){
		return camera;
	}
	
	public Vector2 screenToWorldCords(float screenX, float screenY){
		Vector3 pos = new Vector3(screenX, screenY, 0);
		pos.set(camera.unproject(pos));
		return new Vector2(pos.x, pos.y);
	}
	
	//Camera Controller Methods
	public void translate(float deltaX, float deltaY){
		camera.translate(deltaX, deltaY);
	}
	
   public void setTarget(Vector2 target){
      this.target = target;
  }

  public void smoothFollow(Vector2 target){
      smooth = true;
      this.target = target;
  }
  
  public void goTo(float posX, float posY){
     target = null;
     camera.position.set(posX,posY,0);
 }

 public void goToSmooth(Vector2 position){
     target = position.cpy();
     smooth = true;
 }

}
