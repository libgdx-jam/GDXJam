package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SteerableComponent extends Component implements Steerable<Vector2> {
    //TODO refactor this craziness, not sure if a Component can hold another...Maybe one single PhysicsComponent would be easier...
    PositionComponent positionComponent;
    VelocityComponent velocityComponent;
    RigidBodyComponent rigidBodyComponent;
    OrientationComponent orientationComponent;
    AngularVelocityComponent angularVelocityComponent;
    ForceComponent forceComponent;
    TorqueComponent torqueComponent;

    //TODO values needs to go in constructor, or a class to hold them
    private float maxLinearSpeed = 10;
    private float maxLinearAcceleration = 5;
    private float maxAngularSpeed = 2;
    private float maxAngularAcceleration = 2;
    private boolean tagged;

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
    SteeringBehavior<Vector2> steeringBehavior;

    public SteerableComponent(PositionComponent positionComponent, VelocityComponent velocityComponent,
                              RigidBodyComponent rigidBodyComponent, OrientationComponent orientationComponent,
                              AngularVelocityComponent angularVelocityComponent, ForceComponent forceComponent,
                              TorqueComponent torqueComponent) {
        this.positionComponent = positionComponent;
        this.velocityComponent = velocityComponent;
        this.rigidBodyComponent = rigidBodyComponent;
        this.orientationComponent = orientationComponent;
        this.angularVelocityComponent = angularVelocityComponent;
        this.forceComponent = forceComponent;
        this.torqueComponent = torqueComponent;
    }

    public SteerableComponent(Entity entity) {
        this(entity.getComponent(PositionComponent.class),entity.getComponent(VelocityComponent.class),entity.getComponent(RigidBodyComponent.class),entity.getComponent(OrientationComponent.class),
                entity.getComponent(AngularVelocityComponent.class),entity.getComponent(ForceComponent.class),entity.getComponent(TorqueComponent.class));
    }

    @Override
    public Vector2 getPosition() {
        return positionComponent.pos;
    }

    @Override
    public float getOrientation() {
        return (float)Math.atan2(orientationComponent.getHeading().y,orientationComponent.getHeading().x);
    }

    @Override
    public Vector2 getLinearVelocity() {
        return velocityComponent.velocity;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocityComponent.angularVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return rigidBodyComponent.getBoundRadius();
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Vector2 newVector() {
        return new Vector2();
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(vector.y, vector.x);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = (float)Math.cos(angle);
        outVector.y = (float)Math.sin(angle);

        return outVector;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    public SteeringBehavior<Vector2> getSteeringBehavior () {
        return steeringBehavior;
    }

    public void setSteeringBehavior (SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    public static SteeringAcceleration<Vector2> getSteeringOutput() {
        return steeringOutput;
    }

    public PositionComponent getPositionComponent() {
        return positionComponent;
    }

    public VelocityComponent getVelocityComponent() {
        return velocityComponent;
    }

    public RigidBodyComponent getRigidBodyComponent() {
        return rigidBodyComponent;
    }

    public OrientationComponent getOrientationComponent() {
        return orientationComponent;
    }

    public AngularVelocityComponent getAngularVelocityComponent() {
        return angularVelocityComponent;
    }

    public ForceComponent getForceComponent() {
        return forceComponent;
    }

    public TorqueComponent getTorqueComponent() {
        return torqueComponent;
    }
}
