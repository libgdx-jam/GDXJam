package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;

/**
 * Created by SCAW on 16/02/2015.
 */
public class StateMachineComponent extends Component implements Telegraph {
    public StackStateMachine<Entity> stateMachine;

    public StateMachineComponent init(StackStateMachine<Entity> stateMachine){
        this.stateMachine = stateMachine;
        return this;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }
}
