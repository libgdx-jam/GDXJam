package com.gdxjam.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gdxjam.ai.Messages;
import com.gdxjam.components.CommanderComponent;
import com.gdxjam.components.Components;
import com.gdxjam.systems.CameraSystem;
import com.gdxjam.systems.CommanderControllerSystem;

import java.util.HashMap;

/**
 * Created by SCAW on 18/02/2015.
 */
public class GUItest extends Stage{
    Engine engine;
    Table table;
    Skin skin;
    HashMap<Entity, Actor> commanderButtonMap;
    public GUItest(final Engine engine) {
        super(new StretchViewport(16*80,9*80));
        this.engine = engine;
        commanderButtonMap = new HashMap<Entity,Actor>();

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        table = new Table(skin);

        final VerticalGroup commanderGroup = new VerticalGroup();

        final TextButton newCommanderButton = new TextButton("new", skin);
        newCommanderButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EntityFactory.createSquad(new Vector2((float)Math.random()*50f,(float)Math.random()*50f));
            }
        });
        commanderGroup.addActor(newCommanderButton);

        //Listener to listen when a new Commander/squad is added to the engine
        engine.addEntityListener(Family.all(CommanderComponent.class).get(), new EntityListener() {
            @Override
            public void entityAdded(final Entity commander) {
                TextButton textButton = new TextButton("COMMANDER " + (commanderGroup.getChildren().size +""), skin);
                textButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        engine.getSystem(CommanderControllerSystem.class).selectedCommander = commander;
                        engine.getSystem(CameraSystem.class).goToSmooth(Components.STEERABLE_BODY.get(commander).getPosition());

                        if (getTapCount() > 1) {
                            engine.getSystem(CameraSystem.class).smoothFollow(Components.STEERABLE_BODY.get(commander).getPosition());
                        }
                        HorizontalGroup horizontalGroup = new HorizontalGroup();
                        Label numberOfUnitsLabel = new Label("Units: " + Components.COMMANDER.get(commander).units.size, skin);
                        horizontalGroup.addActor(numberOfUnitsLabel);

                        TextButton regroupB = new TextButton("Regroup", skin);
                        regroupB.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                for(Entity e : Components.COMMANDER.get(commander).units) {
                                    MessageManager.getInstance().dispatchMessage(0f, Components.COMMANDER.get(commander), Components.STATE_MACHINE.get(e), Messages.REGROUP_ORDER);
                                }
                            }
                        });
                        horizontalGroup.addActor(regroupB);

                        TextButton resourcesB = new TextButton("Resources", skin);
                        horizontalGroup.addActor(resourcesB);

                        TextButton attackB = new TextButton("Attack", skin);
                        horizontalGroup.addActor(attackB);

                        horizontalGroup.setFillParent(true);
                        horizontalGroup.bottom().space(6);
                        GUItest.this.addActor(horizontalGroup);
                    }
                });
                commanderButtonMap.put(commander, textButton);
                commanderGroup.addActorBefore(newCommanderButton, textButton);

            }

            @Override
            public void entityRemoved(Entity entity) {
                commanderGroup.removeActor(commanderButtonMap.get(entity));
                commanderButtonMap.remove(entity);
            }
        });

        VerticalGroup commandsExplanation = new VerticalGroup();
        commandsExplanation.addActor(new Label("Commands:", skin));
        commandsExplanation.addActor(new Label("Select Commander on th RightTab", skin));
        commandsExplanation.addActor(new Label("Double click to camera follow commander", skin));
        commandsExplanation.addActor(new Label("'Regroup Order' for units follow commander", skin));
        commandsExplanation.addActor(new Label("Mouse scroll to zoom", skin));
        commandsExplanation.addActor(new Label("'new' to add a new commander on random position", skin));
        commandsExplanation.setFillParent(true);
        commandsExplanation.left();
        this.addActor(commandsExplanation);

        commanderGroup.setFillParent(true);
        commanderGroup.right().space(3f).padRight(2f).padTop(2f);
        this.addActor(commanderGroup);
    }


}
