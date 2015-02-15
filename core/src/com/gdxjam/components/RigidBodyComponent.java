package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Shape2D;

/**
 * Created by SCAW on 15/02/2015.
 */
public abstract class RigidBodyComponent extends Component {
    public Shape2D bounds;
    public boolean rigid;

    public RigidBodyComponent(Shape2D bounds, boolean rigid){
        this.bounds = bounds;
        this.rigid = rigid;
    }

    public abstract float getBoundRadius();
}
