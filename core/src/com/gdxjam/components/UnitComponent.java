package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.gdxjam.ai.Squad;

public class UnitComponent extends Component implements Poolable {

	public Squad squad;
	public Entity target;
	public boolean isCommander;

	@Override
	public void reset() {
		squad = null;
		target = null;
		isCommander = false;
	}

}
