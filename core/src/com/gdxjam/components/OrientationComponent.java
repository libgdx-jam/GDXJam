package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SCAW on 15/02/2015.
 */
public class OrientationComponent extends Component{
    private Vector2 heading;

    public OrientationComponent(Vector2 heading) {
        this.heading = heading.nor();
    }

    public OrientationComponent(float x, float y) {
        this(new Vector2(x,y));
    }

    public Vector2 getHeading() {
        return heading;
    }

    public void setHeading(Vector2 heading) {
        this.heading = heading.nor();
    }

    public float getAngleRad(){
        return (float)Math.atan2(heading.y,heading.x);
    }

    public float getAngleDeg(){
        return (float)Math.toDegrees(getAngleRad());
    }
}
