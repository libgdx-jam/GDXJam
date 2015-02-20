package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Array;

/**
 * Created by SCAW on 17/02/2015.
 */
public class CommanderComponent extends Component implements Telegraph{
    public Array<Entity> units;

    public CommanderComponent init(Array<Entity> units){
        this.units = units;
        return this;
    }

    public void addUnit(Entity unit){
        units.add(unit);
    }

    public void removeUnit(Entity unit){
        units.removeValue(unit, true);
    }


    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
