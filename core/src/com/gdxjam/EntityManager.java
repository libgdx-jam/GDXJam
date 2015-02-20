package com.gdxjam;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.EntityRenderSystem;
import com.gdxjam.systems.GameWorldSystem;
import com.gdxjam.systems.PhysicsSystem;
import com.gdxjam.systems.ResourceSystem;
import com.gdxjam.systems.SquadSystem;
import com.gdxjam.systems.StateMachineSystem;
import com.gdxjam.systems.SteeringSystem;
import com.gdxjam.utils.EntityFactory;

public class EntityManager extends PooledEngine implements Disposable{
	private static String TAG = "[" + EntityManager.class.getSimpleName() + "]";
	private static EntityManager instance;
	public static synchronized EntityManager getInstance(){
		if(instance == null){
			instance = new EntityManager();
		}
		return instance;
	}
	
	public EntityManager(){
		Gdx.app.log(TAG, "initalized instance");
	}
	
	public void initSystems(){
		EntityFactory.setEngine(this);

		CameraSystem cameraSystem = new CameraSystem(64, 36);
		cameraSystem.getCamera().position.set(32, 18, 0);
		addSystem(cameraSystem);

		addSystem(new PhysicsSystem());

		//AI
		addSystem( new SteeringSystem());
		addSystem(new StateMachineSystem());
		addSystem(new SquadSystem());
		
		
		addSystem(new EntityRenderSystem(cameraSystem.getCamera()));
	}
	
	public void loadWorld(GameWorld world){
		addSystem(new ResourceSystem(world));
		addSystem(new GameWorldSystem(world));
	}

	@Override
	public void dispose () {
		removeAllEntities();
		clearPools();
		for(EntitySystem system : getSystems()){
			if(system instanceof Disposable){
				((Disposable)system).dispose();
			}
			system = null;
			removeSystem(system);
		}
		
		instance = null;
	}
	
}
