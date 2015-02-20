package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SCAW on 16/02/2015.
 */
public class ProximityComponent extends Component {
    public Proximity<Vector2> proximity;

    public ProximityComponent init(Proximity<Vector2> proximity){
        this.proximity = proximity;
        return this;
    }
}
