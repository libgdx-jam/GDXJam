
package com.gdxjam.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.utils.Constants;

public class CommandCardContainer extends Table {

	private final Skin skin;

	private Array<CommandCardSlot> slots;

	private static final int slotWidth = 216;
	private static final int slotHeight = 100;

	private DragAndDrop dragAndDrop;

	public CommandCardContainer (final Skin skin, Stage stage) {
		this.skin = skin;

		slots = new Array<CommandCardSlot>(Constants.maxSquads);
		dragAndDrop = new DragAndDrop();

		for (int i = 0; i < Constants.maxSquads; i++) {
			final CommandCardSlot slot = new CommandCardSlot(skin);
			slots.add(slot);
			add(slot).size(slotWidth, slotHeight);

			Target target = new Target(slot) {

				@Override
				public void drop (Source source, Payload payload, float x, float y, int pointer) {
					CommandCardSlot slotA = slot;
					CommandCardSlot slotB = (CommandCardSlot)payload.getObject();

					CommandCard cardA = (CommandCard)source.getActor();
					CommandCard cardB = (CommandCard)slotA.getUserObject();

					slotA.setCard(cardA);
					slotB.setCard(cardB);
				}

				@Override
				public boolean drag (Source source, Payload payload, float x, float y, int pointer) {
					return true;
				}
			};

			dragAndDrop.addTarget(target);

			final EmptyCommandCard card = new EmptyCommandCard(skin);
			slot.setCard(card);

			Source source = new Source(card) {

				@Override
				public Payload dragStart (InputEvent event, float x, float y, int pointer) {
					Payload payload = new Payload();
					payload.setObject(card.getUserObject());
					payload.setDragActor(new Label("Test", skin));
					return payload;
				}
			};

			dragAndDrop.addSource(source);

		}

	}

	public void addSquad (final Entity squad, final int index) {
		final SquadCommandCard squadTable = new SquadCommandCard(squad, index, skin);
		Source source = new Source(squadTable) {

			@Override
			public Payload dragStart (InputEvent event, float x, float y, int pointer) {
				Payload payload = new Payload();
				payload.setObject(squadTable.getUserObject());
// payload.setDragActor(new Label("Test", skin));
				SquadCommandCard card = new SquadCommandCard(squad, index, skin);
				payload.setDragActor(card);
// payload.setDragActor(squadTable);
				return payload;
			}
		};

		dragAndDrop.addSource(source);

		slots.get(index).setCard(squadTable);

	}

	public void removeSquad (Entity squad, int index) {

	}

	public void updateSquadTable (int index) {

	}

	public Entity setSelected (int index, boolean selected) {
		SquadCommandCard card = (SquadCommandCard)slots.get(index).getCard();
		card.setSelected(selected);
		return card.getSquad();
	}
}
