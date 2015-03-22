
package com.gdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.gdxjam.ai.state.TelegramMessage;
import com.gdxjam.ecs.Components;
import com.gdxjam.utils.EntityFactory;

public class ConstructionSystem implements Telegraph {

	public ResourceSystem resourceSystem;

	private static final int unitCost = 1000;

	public ConstructionSystem (ResourceSystem resourceSystem) {
		this.resourceSystem = resourceSystem;

		// Add message listener
		MessageManager.getInstance().addListener(this, TelegramMessage.CONSTRUCT_UNIT_REQUEST.ordinal());
	}

	public void constructUnit (Entity squad) {
		if (resourceSystem.resources >= unitCost) {
			resourceSystem.resources -= unitCost;
			EntityFactory.createUnit(squad);
			MessageManager.getInstance().dispatchMessage(this, Components.FSM.get(squad),
				TelegramMessage.CONSTRUCT_UNIT_CONFRIM.ordinal());
		} else {
			MessageManager.getInstance().dispatchMessage(TelegramMessage.GUI_INSUFFICIENT_RESOURCES.ordinal());
		}
	}

	@Override
	public boolean handleMessage (Telegram msg) {
		TelegramMessage telegramMsg = TelegramMessage.values()[msg.message];
		switch (telegramMsg) {

		case CONSTRUCT_UNIT_REQUEST:
			Entity squad = (Entity)msg.extraInfo;
			constructUnit(squad);
			return true;

		default:
			return false;
		}

	}

}
