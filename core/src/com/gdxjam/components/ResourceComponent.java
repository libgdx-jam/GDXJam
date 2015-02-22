package com.gdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by SCAW on 17/02/2015.
 */
public abstract class ResourceComponent extends Component {

    public enum ResourceType {
        WOOD(LumberComponent.class),
        FOOD(ResourceComponent.class),	//TODO add food and stone(iron?) components
        STONE(ResourceComponent.class);
        
        public Class<? extends ResourceComponent> component;
        private ResourceType(Class<? extends ResourceComponent> component){
      	  this.component = component;
        }
    }

    public ResourceType resourceType;
    public int amount; 

}
