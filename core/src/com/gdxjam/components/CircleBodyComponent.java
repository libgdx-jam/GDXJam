package com.gdxjam.components;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;

/**
 * Created by SCAW on 15/02/2015.
 */
public class CircleBodyComponent extends RigidBodyComponent {

    public CircleBodyComponent(float radius, boolean rigid) {
        super(new Circle(0,0,radius), rigid);
    }

    @Override
    public float getBoundRadius() {
        return 0;
    }
}
