package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by SCAW on 16/02/2015.
 */
public class CommanderHolderComponent extends Component {
    public Entity general;

    public CommanderHolderComponent init(Entity genral){
        this.general = genral;
        return this;
    }
}
