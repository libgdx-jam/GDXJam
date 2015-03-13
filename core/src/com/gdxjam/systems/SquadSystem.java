
package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.ecs.Components;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityFactory;

public class SquadSystem extends IteratingSystem {

	private static final String TAG = "[" + SquadSystem.class.getSimpleName() + "]";
	private InputSystem inputSystem;

	public SquadSystem (InputSystem inputSystem) {
		super(Family.all(SquadComponent.class).get());
		this.inputSystem = inputSystem;
	}


	public void createPlayerSquad (Vector2 position, Faction faction, int members) {
		Entity squad = createSquad(position, faction, members);
		inputSystem.addSquad(squad);
	}

	public void createPlayerSquad (int index, Vector2 position, Faction faction, int members) {
		Entity squad = createSquad(position, faction, members);
		inputSystem.addSquad(squad, index);
	}

	public Entity createSquad (Vector2 position, Faction faction, int members) {
		Entity squad = EntityFactory.createSquad(position, faction);
		int columns = (int)Math.sqrt(members);
		int posX = (int)position.x;
		int posY = (int)position.y;
		for (int i = 0; i < members; i++) {
			int x = i / columns;
			int y = i % columns;
			EntityFactory.createUnit(new Vector2(posX + x, posY + y), squad);
		}

		if (faction != Constants.playerFaction) {
			Components.SQUAD.get(squad).setTarget(new Vector2(128, 128)); // TODO make dynamic
		}
		
		return squad;
	}

	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
	}

	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	protected void processEntity (Entity entity, float deltaTime) {
		SquadComponent squadComp = Components.SQUAD.get(entity);
		squadComp.formation.updateSlots();
	}

}
