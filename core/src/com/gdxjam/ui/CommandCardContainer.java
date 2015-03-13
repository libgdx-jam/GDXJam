
package com.gdxjam.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.IntMap;
import com.gdxjam.components.SquadComponent.FormationPatternType;
import com.gdxjam.systems.InputSystem;
import com.gdxjam.utils.Constants;

public class CommandCardContainer extends Table {

	private final Skin skin;

	private IntMap<CommandCardSlot> cardSlots = new IntMap<CommandCardSlot>();

	private static final int slotWidth = 216;
	private static final int slotHeight = 100;

	private DragAndDrop dragAndDrop;
	private InputSystem inputSystem;

	public CommandCardContainer (final InputSystem inputSystem, final Skin skin, Stage stage) {
		this.skin = skin;
		this.inputSystem = inputSystem;

		dragAndDrop = new DragAndDrop();

		for (int i = 0; i < Constants.maxSquads; i++) {
			final CommandCardSlot slot = new CommandCardSlot(i, skin);
			cardSlots.put(i, slot);
			add(slot).size(slotWidth, slotHeight);

			Target target = new Target(slot) {

				@Override
				public void drop (Source source, Payload payload, float x, float y, int pointer) {
					CommandCardSlot slotA = slot;
					CommandCardSlot slotB = cardSlots.get((Integer)source.getActor().getUserObject());

					CommandCard cardA = (CommandCard)source.getActor();
					CommandCard cardB = (CommandCard)slotA.getUserObject();

					slotA.setCard(cardA);
					slotB.setCard(cardB);

					inputSystem.swapSquadSlot(slotA.index, slotB.index);
				}

				@Override
				public boolean drag (Source source, Payload payload, float x, float y, int pointer) {
					return true;
				}
			};

			dragAndDrop.addTarget(target);

			addSquad(null, i);
		}

	}

	public void addSquad (final Entity squad, final int index) {
		final SquadCommandCard squadCard = new SquadCommandCard(squad, index, skin);
		Source source = new Source(squadCard) {

			@Override
			public Payload dragStart (InputEvent event, float x, float y, int pointer) {
				Payload payload = new Payload();

// payload.setDragActor(new Label("Test", skin));
				SquadCommandCard card = new SquadCommandCard(squad, index, skin);
				payload.setDragActor(card);
// payload.setDragActor(squadTable);
				return payload;
			}
		};

		dragAndDrop.addSource(source);

		cardSlots.get(index).setCard(squadCard);
	}

	public void removeSquad (Entity squad, int index) {
		SquadCommandCard card = (SquadCommandCard)cardSlots.get(index).getCard();
		card.setSquad(null);
		card.setSelected(false);
	}

	public void updateFormationPattern (int index, FormationPatternType pattern) {
		SquadCommandCard card = (SquadCommandCard)cardSlots.get(index).getCard();
		card.updateFormationPattern(pattern);
	}

	public void updateSquadTable (int index) {
		SquadCommandCard card = (SquadCommandCard)cardSlots.get(index).getCard();
	}

	public Entity setSelected (int index, boolean selected) {
		SquadCommandCard card = (SquadCommandCard)cardSlots.get(index).getCard();
		card.setSelected(selected);
		return card.getSquad();
	}
}
