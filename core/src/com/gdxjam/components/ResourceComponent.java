package com.gdxjam.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by SCAW on 17/02/2015.
 */
public abstract class ResourceComponent extends Component {

    public enum ResourceType {
        WOOD("Wood", LumberComponent.class),
        FOOD("Food", ResourceComponent.class),	//TODO add food and stone(iron?) components
        STONE("Stone", ResourceComponent.class);
        
        public String name;
        public Class<? extends ResourceComponent> component;
        
        private ResourceType(String name, Class<? extends ResourceComponent> component){
      	  this.name = name;
      	  this.component = component;
        }
    }

    public ResourceType resourceType;
    public int amount; 

}
