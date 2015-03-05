package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;

public class SquadSpawnerSystem extends EntitySystem{
	
	private static Vector2 spawnPosition = new Vector2();
	
	public SquadSpawnerSystem () {

	}
	
	public static void initalizeSpawns(){
		Task task = new Task() {
			
			@Override
			public void run () {
				SquadSpawnerSystem.spawnSquad();
			}
		};
		
		Timer.schedule(task, 2.0f, 2.0f);
	}
	
	public static void spawnSquad(){
		spawnPosition.set(MathUtils.random(100, 140), MathUtils.random(100, 140));
		
		Entity squad = EntityFactory.createSquad(spawnPosition, Constants.enemyFaction);
		int posX = (int)spawnPosition.x;
		int posY = (int)spawnPosition.y;
		for(int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++){
				EntityFactory.createUnit(new Vector2(posX + x, posY + y), squad);
			}
		}
	}
	
	@Override
	public void update (float deltaTime) {
		// TODO Auto-generated method stub
		super.update(deltaTime);
	}

}
