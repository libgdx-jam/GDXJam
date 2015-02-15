package com.gdxjam.ai.test;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.Assets;
import com.gdxjam.components.*;
import com.gdxjam.systems.MovementSystem;
import com.gdxjam.systems.RenderSystem;
import com.gdxjam.systems.SteerableSystem;

import java.lang.annotation.Target;

/**
 * Created by SCAW on 15/02/2015.
 */
public class TestScreen implements Screen {
    SpriteBatch batch;
    OrthographicCamera camera;
    private PooledEngine engine;

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(100, 100);
        camera.position.set(new Vector2(50f,50f),0);
        camera.update();

        engine = new PooledEngine();
        engine.addSystem(new RenderSystem(camera));
        engine.addSystem(new SteerableSystem());
        engine.addSystem(new MovementSystem());


        Entity target = new Entity();
        target
                .add(new PositionComponent(50,40)).add(new VelocityComponent(0,0))
                .add(new OrientationComponent(-1, 0)).add(new AngularVelocityComponent(0))
                .add(new ForceComponent()).add(new TorqueComponent())
                .add(new CircleBodyComponent(10,true))
                .add(new VisualComponent(Assets.instance.chest.reg, 0));
        SteerableComponent targetSteerableComponent = new SteerableComponent(target);
        target.add(targetSteerableComponent);

        Wander<Vector2> wander = new Wander<Vector2>(targetSteerableComponent)
                .setWanderOffset(0.5f) //
                .setWanderOrientation(0) //
                .setWanderRadius(.6f) //
                .setWanderRate(MathUtils.PI / 4)
                .setFaceEnabled(true)
                .setAlignTolerance(0.001f) // Used by Face
                .setDecelerationRadius(5) // Used by Face
                .setTimeToTarget(0.1f); // Used by Face;

        targetSteerableComponent.setSteeringBehavior(wander);

        Entity mover = new Entity();
        mover
                .add(new PositionComponent(0, 0)).add(new VelocityComponent(0, 0))
                .add(new OrientationComponent(0, 1)).add(new AngularVelocityComponent(0))
                .add(new ForceComponent()).add(new TorqueComponent())
                .add(new CircleBodyComponent(10,true))
                .add(new VisualComponent(Assets.instance.chest.reg, 0));
        SteerableComponent moverSteerablecomponent = new SteerableComponent(mover);
        mover.add(moverSteerablecomponent);

        Arrive<Vector2> arriveSB = new Arrive<Vector2>(moverSteerablecomponent, targetSteerableComponent) //
                .setTimeToTarget(0.01f) //
                .setArrivalTolerance(0.001f) //
                .setDecelerationRadius(5);

        LookWhereYouAreGoing<Vector2> lookWhereYouAreGoing = new LookWhereYouAreGoing<Vector2>(moverSteerablecomponent) //
                .setTimeToTarget(0.05f) //
                .setAlignTolerance(0.001f) //
                .setDecelerationRadius(MathUtils.PI/4);

        BlendedSteering<Vector2> blendedSteering = new BlendedSteering<Vector2>(
                moverSteerablecomponent) //
                .setLimiter(NullLimiter.NEUTRAL_LIMITER) //
                .add(arriveSB, 1f) //
                .add(lookWhereYouAreGoing, 1f);


        moverSteerablecomponent.setSteeringBehavior(blendedSteering);

        engine.addEntity(target);
        engine.addEntity(mover);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
