package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.NinePatch;

public class NinePatchComponent extends Component{

	public NinePatch patch;
	
	public NinePatchComponent init(NinePatch patch){
		this.patch = patch;
		return this;
	}
	
}
