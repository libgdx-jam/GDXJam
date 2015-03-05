
package com.gdxjam;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdxjam.components.Components;
import com.gdxjam.components.FactionComponent;
import com.gdxjam.components.HealthComponent;
import com.gdxjam.components.ProjectileComponent;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityUtils;

public class GameContactListener implements ContactListener {

	@Override
	public void beginContact (Contact contact) {
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();

		if (Components.PROJECTILE.has(entityA)) {
			processProjectile(entityA, entityB);
		}
		if (Components.PROJECTILE.has(entityB)) {
			processProjectile(entityB, entityA);
		}
		
	}

	@Override
	public void endContact (Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve (Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve (Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	public void processProjectile (Entity projectile, Entity target) {
		//If what we hit is also a projectile do nothing
		if(Components.PROJECTILE.has(target)) return;
			
		if (Components.HEALTH.has(target)) {
			if(!Constants.friendlyFire){
				FactionComponent projectileFactionComp = Components.FACTION.get(projectile);
				FactionComponent targetFactionComp = Components.FACTION.get(target);
				if(projectileFactionComp.faction == targetFactionComp.faction)
					return;
			}
			
			if(Components.SQUAD.has(target))
				return;
			
			ProjectileComponent projectileComp = Components.PROJECTILE.get(projectile);
			HealthComponent healthComp = Components.HEALTH.get(target);

			healthComp.value -= projectileComp.damage;
		}

		EntityUtils.removeEntity(projectile);
	}

}
