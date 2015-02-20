package com.gdxjam;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gdxjam.ai.Messages;
import com.gdxjam.components.Components;

/**
 * Created by SCAW on 17/02/2015.
 */
public class InputAITest implements InputProcessor {
    OrthographicCamera camera;
    Vector2 target;
    Entity commander;

    public InputAITest(OrthographicCamera camera, Vector2 target, Entity commander) {
        this.camera = camera;
        this.target = target;
        this.commander = commander;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.R:
                for(Entity e : Components.COMMANDER.get(commander).units){
                    MessageManager.getInstance().dispatchMessage(0f, Components.COMMANDER.get(commander), Components.STATE_MACHINE.get(e), Messages.REGROUP_ORDER);
                }
                return true;


        }

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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        pos.set(camera.unproject(pos));
        target.set(pos.x,pos.y);
        return true;
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
        return false;
    }
}
