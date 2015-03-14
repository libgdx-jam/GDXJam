
package com.gdxjam.utils;

import sun.org.mozilla.javascript.internal.Token.CommentType;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.ai.state.Messages;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.components.WeaponComponent;
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

	public static void clearTarget (Entity entity) {
		ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(TargetComponent.class).get());
		for (Entity e : entities) {
			TargetComponent targetComp = Components.TARGET.get(e);
			if (targetComp.getTarget() == entity) {
				targetComp.setTarget(null);

				if (Components.FSM.has(e)) {
					FSMComponent fsm = Components.FSM.get(e);
					MessageManager.getInstance().dispatchMessage(null, fsm, Messages.targetDestroyed, entity);
				}
			}

		}
	}

	public static void removeEntity (Entity entity) {
		engine.removeEntity(entity);
	}

}
