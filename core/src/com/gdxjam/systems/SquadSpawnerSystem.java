package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.gdxjam.components.Components;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;

public class SquadSpawnerSystem extends EntitySystem{
	
	public static Array<Vector2> spawnPoints = new Array<Vector2>();
	
	public SquadSpawnerSystem () {

	}
	
	public static void addSpawnPoint(Vector2 spawnPoint){
		spawnPoints.add(spawnPoint);
	}
	
	public static void initalizeSpawns(){
		Task task = new Task() {
			
			@Override
			public void run () {
				SquadSpawnerSystem.spawnSquad(spawnPoints.random(), Constants.enemyFaction, 3);
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
		
		if(faction != Constants.playerFaction){
			Components.SQUAD.get(squad).setTarget(new Vector2(128, 128)); 	//TODO make dynamic
		}
	}
	
	@Override
	public void update (float deltaTime) {
		// TODO Auto-generated method stub
		super.update(deltaTime);
	}

}
