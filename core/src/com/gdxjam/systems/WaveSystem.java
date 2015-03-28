package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.gdxjam.GameManager;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.ecs.Components;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;

public class WaveSystem extends EntitySystem{
	
	public static Array<Vector2> spawnPoints = new Array<Vector2>();
	
	private GUISystem guiSystem;
	private WaveParam nextWave;
	private float nextWaveTime = 90;
	
	public WaveSystem (GUISystem guiSystem) {
		this.guiSystem = guiSystem;
		
		initalizeNextWave();
	}
	
	public static void addSpawnPoint(Vector2 spawnPoint){
		spawnPoints.add(spawnPoint);
	}
	
	public static void initalizeSpawns(){
		Task task = new Task() {
			
			@Override
			public void run () {
				WaveSystem.spawnSquad(spawnPoints.random(), Constants.enemyFaction, 3);
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
	
	
	private void initalizeNextWave(){
		WaveParam wave = new WaveParam();
		wave.delay = MathUtils.random(120, 180);
		wave.squadCount = MathUtils.random(3, 6);
		
		nextWave = wave;
		nextWaveTime = wave.delay;
	}
	
	private void beginWave(WaveParam wave){
		Task task = new Task() {
			
			@Override
			public void run () {
				WaveSystem.spawnSquad(spawnPoints.random(), Constants.enemyFaction, 5);
			}
		};
		
		Timer.schedule(task, 0.0f, wave.spawnInterval, wave.squadCount);
	}
	
	public void setTimeToNextWave(float time){
		this.nextWaveTime = time;
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		guiSystem.updateWaveTime(nextWaveTime);
		nextWaveTime -= deltaTime;
		
		if(nextWaveTime <= 0){
			beginWave(nextWave);
			initalizeNextWave();
		}
	}
	
	@Override
	public boolean checkProcessing () {
		return !GameManager.isPaused();
	}
	
	public class WaveParam{
		public float delay = 100;
		public int squadCount = 3;
		public int directions = 5;
		public float spawnInterval = 5.0f;
	}

}
