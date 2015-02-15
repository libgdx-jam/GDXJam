package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SCAW on 15/02/2015.
 */
public class ForceComponent extends Component {
    public Vector2 force;

    public ForceComponent(Vector2 force) {
        this.force = force;
    }
    public ForceComponent(){
        this(new Vector2(0,0));
    }
}
