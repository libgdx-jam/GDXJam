package com.gdxjam.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by SCAW on 18/02/2015.
 */
public class CameraController {
    OrthographicCamera camera;
    public Vector2 toFollow = null;
    boolean smooth = false;

    public CameraController(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void update(float deltaTime){
        if (smooth && toFollow!= null){
            //newPosition = new Vector3(toFollow.x,toFollow.y,0).sub(camera.position).sc
            camera.position.add(camera.position.cpy().scl(-1).add(toFollow.x,toFollow.y,0).scl(0.04f));
        }

        camera.update();
    }

    public void setToFollow(Vector2 toFollow){
        this.toFollow = toFollow;
    }

    public void smoothFollow(Vector2 toFollow){
        smooth = true;
        this.toFollow = toFollow;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void goTo(Vector2 position){
        goTo(position.x,position.y);
    }

    public void goTo(float posX, float posY){
        toFollow = null;
        camera.position.set(posX,posY,0);
    }

    public void goToSmooth(Vector2 position){
        toFollow = position.cpy();
        smooth = true;
    }

}
