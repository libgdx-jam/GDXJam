package com.gdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by SCAW on 15/02/2015.
 */
public class TorqueComponent extends Component {
    public float torque;

    public TorqueComponent(float torque) {
        this.torque = torque;
    }
    public TorqueComponent(){
        this(0);
    }
}
