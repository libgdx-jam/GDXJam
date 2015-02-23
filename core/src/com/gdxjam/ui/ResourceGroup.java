package com.gdxjam.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntMap;
import com.gdxjam.components.ResourceComponent.ResourceType;

public class ResourceGroup extends Table{
	
	private Skin skin;

	private IntMap<ResourceTable> resourceMap = new IntMap<ResourceTable>();
	
	public ResourceGroup(Skin skin){
		this.skin = skin;
		defaults().pad(5);
		
		for(int i = 0; i < ResourceType.values().length; i++){
			addResource(ResourceType.values()[i]);
		}
	}
	
	public void addResource(ResourceType type){
		ResourceTable table = new ResourceTable(type, skin);
		resourceMap.put(type.ordinal(), table);
		add(table);
	}
	
	public void update(ResourceType type, int amount){
		resourceMap.get(type.ordinal()).update(amount);
	}

}
