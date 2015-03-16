
package com.gdxjam;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.state.TelegramMessage;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.ProjectileComponent;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityUtils;

public class GameContactListener implements ContactListener {

	@SuppressWarnings("unused")
	private static final String TAG = GameContactListener.class.getSimpleName();

	@Override
	public void beginContact (Contact contact) {
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();

		if (Components.PROJECTILE.has(entityA)) processProjectile(entityA, entityB);
		if (Components.PROJECTILE.has(entityB)) processProjectile(entityB, entityA);

		if (Components.SQUAD.has(entityA)) processTargetFinder(entityA, entityB, false);
		if (Components.SQUAD.has(entityB)) processTargetFinder(entityB, entityA, false);

	}

	@Override
	public void endContact (Contact contact) {
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();

		if (Components.SQUAD.has(entityA)) processTargetFinder(entityA, entityB, true);
		if (Components.SQUAD.has(entityB)) processTargetFinder(entityB, entityA, true);
	}

	@Override
	public void preSolve (Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve (Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	public void processTargetFinder (Entity squad, Entity target, boolean contactEnd) {
		// return if the two entities are allies
		if (EntityUtils.isSameFaction(squad, target)) return;

		SquadComponent squadComp = Components.SQUAD.get(squad);
		if (Components.SQUAD.has(target)) {
			squadComp.modifyTargetsInRange(squadComp.enemiesInRange, target, !contactEnd);
			// Sends a message to the squad that a new target has been identified.
			if (!contactEnd) // if this is a beging contact
				MessageManager.getInstance().dispatchMessage(null, Components.FSM.get(squad),
					TelegramMessage.SQUAD_DISCOVERED_ENEMY.ordinal());
		} else if (Components.RESOURCE.has(target)) {
			squadComp.modifyTargetsInRange(squadComp.resourcesInRange, target, !contactEnd);
			sortResources(squad);
			// Sends a message to the squad that a new resource has been identified.
			if (!contactEnd) // if this is when the contact begins
				MessageManager.getInstance().dispatchMessage(null, Components.FSM.get(squad),
					TelegramMessage.SQUAD_DISCOVERED_RESOURCE.ordinal());
		}
	}

	public void sortResources (final Entity squad) {
		Array<Entity> resources = Components.SQUAD.get(squad).resourcesInRange;
		resources.sort(new Comparator<Entity>() {

			@Override
			public int compare (Entity e1, Entity e2) {
				Vector2 squadPos = Components.PHYSICS.get(squad).getBody().getPosition();
				Vector2 pos1 = Components.PHYSICS.get(e1).getBody().getPosition();
				Vector2 pos2 = Components.PHYSICS.get(e2).getBody().getPosition();

				float dist1 = squadPos.dst2(pos1);
				float dist2 = squadPos.dst2(pos2);

				return dist1 > dist2 ? 1 : -1;
			}

		});
	}

	public void processProjectile (Entity projectile, Entity target) {
		if (Components.HEALTH.has(target)) {
			if (!Constants.friendlyFire) {
				Faction projectileFaction = Components.FACTION.get(projectile).getFaction();
				Faction targetFaction = Components.FACTION.get(target).getFaction();
				if (projectileFaction == targetFaction) return;
			}

			ProjectileComponent projectileComp = Components.PROJECTILE.get(projectile);
			HealthComponent healthComp = Components.HEALTH.get(target);

			healthComp.value -= projectileComp.getDamage();
		}

		EntityUtils.removeEntity(projectile);
	}

}
