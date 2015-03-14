package com.gdxjam.utils;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.WeaponComponent;

public class WorldSpawner {
	
	private static PooledEngine engine;
	
	public static void init(PooledEngine engine){
		WorldSpawner.engine = engine;
	}
	
}
