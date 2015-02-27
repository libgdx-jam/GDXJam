
package com.gdxjam;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdxjam.components.Components;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.TargetFinderComponent;

public class GameContactListener implements ContactListener {

	@Override
	public void beginContact (Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();

		if (fixtureA.getUserData() instanceof TargetFinderComponent) {
			processTargetFinder(fixtureA, fixtureB);
		} else if (fixtureB.getUserData() instanceof TargetFinderComponent) {
			processTargetFinder(fixtureB, fixtureA);
		}

	}

	public void processTargetFinder (Fixture a, Fixture b) {
		TargetFinderComponent targetFinder = (TargetFinderComponent)a.getUserData();
		Entity entity = (Entity)a.getBody().getUserData();

		if (!b.isSensor() && b.getBody().getUserData() instanceof Entity) {
			Entity target = (Entity)b.getBody().getUserData();
			if (Components.RESOURCE.has(target)) {
				targetFinder.addResource(target);
			} else if (Components.UNIT.has(entity) && Components.FACTION.has(entity)) {
				Faction entityFaction = Components.FACTION.get(entity).faction;
				Faction targetFaction = Components.FACTION.get(target).faction;
				if (entityFaction != targetFaction) {
					targetFinder.addEnemy(entity);
				}
			}
		}
	}

	@Override
	public void endContact (Contact contact) {

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
