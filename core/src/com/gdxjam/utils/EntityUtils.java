
package com.gdxjam.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.gdxjam.ai.state.TelegramMessage;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.systems.GUISystem;

public class EntityUtils {

	private static final String TAG = "[" + EntityUtils.class.getSimpleName() + "]";
	private static PooledEngine engine;
	private static GUISystem guiSystem;

	public static void setEngine (PooledEngine engine) {
		EntityUtils.engine = engine;
		EntityUtils.guiSystem = engine.getSystem(GUISystem.class);
	}

	/** Checks to see if two entities are of the same faction
	 * @param entityA
	 * @param entityB
	 * @return true if they are the same faction */
	public static boolean isSameFaction (Entity entityA, Entity entityB) {
		if (!Components.FACTION.has(entityA) || !Components.FACTION.has(entityB)) {
			Gdx.app.error(TAG, "entity faction comparision is missing faction component");
			return false;
		}

		Faction factionA = Components.FACTION.get(entityA).getFaction();
		Faction factionB = Components.FACTION.get(entityB).getFaction();

		return factionA == factionB;
	}

	public static void clearTarget (Entity target) {
		ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(TargetComponent.class).get());
		
		for (Entity entity : entities) {
			TargetComponent targetComp = Components.TARGET.get(entity);
			if (targetComp.getTarget() == target) {
				targetComp.setTarget(null);

				//Dispatch a message to the entites FSM that there target was removed from the engine
				if (Components.FSM.has(entity)) {
					FSMComponent fsm = Components.FSM.get(entity);
					MessageManager.getInstance().dispatchMessage(null, fsm, TelegramMessage.TARGET_REMOVED.ordinal(), target);
				}
			}

		}
	}

	public static void removeEntity (Entity entity) {
		engine.removeEntity(entity);
	}

}
