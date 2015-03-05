package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.gdxjam.components.FactionComponent.Faction;
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
				spawnPosition.set(MathUtils.random(100, 140), MathUtils.random(100, 140));
				SquadSpawnerSystem.spawnSquad(spawnPosition, Constants.enemyFaction, 9);
			}
		};
		
		Timer.schedule(task, 5.0f, 10.0f);
	}
	
	public static void spawnSquad(Vector2 position, Faction faction, int members){
		Entity squad = EntityFactory.createSquad(position, faction);
		int columns = (int)Math.sqrt(members);
		int posX = (int)position.x;
		int posY = (int)position.y;
		for(int i = 0; i < members; i++) {
			int x = i / columns;
			int y = i % columns;
			EntityFactory.createUnit(new Vector2(posX + x, posY + y), squad);
		}
	}
	
	@Override
	public void update (float deltaTime) {
		// TODO Auto-generated method stub
		super.update(deltaTime);
	}

}
