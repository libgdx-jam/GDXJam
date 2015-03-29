
package com.gdxjam;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.ProjectileComponent;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityUtils;

public class GameContactListener implements ContactListener {

	@SuppressWarnings("unused") private static final String TAG = GameContactListener.class.getSimpleName();

	@Override
	public void beginContact (Contact contact) {
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();

		if (Components.PROJECTILE.has(entityA)) processProjectile(entityA, entityB);
		if (Components.PROJECTILE.has(entityB)) processProjectile(entityB, entityA);

		if (Components.SQUAD.has(entityA)) processTargetTracker(entityA, entityB, false);
		if (Components.SQUAD.has(entityB)) processTargetTracker(entityB, entityA, false);

	}

	@Override
	public void endContact (Contact contact) {
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();

		if (Components.SQUAD.has(entityA)) processTargetTracker(entityA, entityB, true);
		if (Components.SQUAD.has(entityB)) processTargetTracker(entityB, entityA, true);
	}

	public void processTargetTracker (Entity squad, Entity target, boolean contactEnd) {
		SquadComponent squadComp = Components.SQUAD.get(squad);
		if (contactEnd)
			squadComp.untrack(squad, target);
		else
			squadComp.track(squad, target);
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

	@Override
	public void preSolve (Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve (Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
