package com.gdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by SCAW on 15/02/2015.
 */
public class AngularVelocityComponent extends Component{
    public float angularVelocity;

    public AngularVelocityComponent(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }
}
