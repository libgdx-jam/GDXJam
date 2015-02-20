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
    public int amount; 

    public ResourceComponent init(ResourceType resourceType, int amount){
        this.resourceType = resourceType;
        this.amount = amount;
        return this;
    }



}
