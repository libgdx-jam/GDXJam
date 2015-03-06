
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;

public class FSMComponent extends Component implements Telegraph {

	private DefaultStateMachine<Entity> stateMachine;

	public FSMComponent init (Entity entity) {
		stateMachine = new DefaultStateMachine<Entity>(entity);
		return this;
	}
	
	public void update(){
		stateMachine.update();
	}
	
	public void changeState(State<Entity> state){
		stateMachine.changeState(state);
	}

	@Override
	public boolean handleMessage (Telegram msg) {
		return stateMachine.handleMessage(msg);
	}
}
