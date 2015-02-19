package com.gdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by SCAW on 17/02/2015.
 */
public class ResourceComponent extends Component {

    public enum ResourceType {
        WOOD, FOOD, STONE
    }

    public ResourceType resourceType;

    public ResourceComponent init(ResourceType resourceType){
        this.resourceType = resourceType;
        return this;
    }



}
