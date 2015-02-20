package com.gdxjam;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.steer.SteerableAdapter;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gdxjam.components.Components;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.CommanderControllerSystem;

/**
 * Created by SCAW on 17/02/2015.
 */
public class InputAITest implements InputProcessor {
    Engine engine;

    public InputAITest(Engine engine) {
        this.engine = engine;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, int pointer, int button) {
        Entity entity = engine.getSystem(CommanderControllerSystem.class).selectedCommander;
        if (entity != null){
            Vector3 pos = new Vector3(screenX, screenY, 0);
            pos.set(engine.getSystem(CameraSystem.class).getCamera().unproject(pos));
            final Vector2 posR =new Vector2(pos.x,pos.y);
            Arrive<Vector2> arrive = new Arrive<Vector2>(Components.STEERABLE_BODY.get(entity), new SteerableAdapter<Vector2>(){
                @Override
                public Vector2 getPosition() {
                    return posR.cpy();

                }
            }).setTimeToTarget(0.01f).setArrivalTolerance(0.002f).setDecelerationRadius(5f);

            Components.STEERING_BEHAVIOR.get(entity).setBehavior(arrive);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        engine.getSystem(CameraSystem.class).getCamera().zoom += amount/10f;
        return false;
    }
}
