package com.gdxjam.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdxjam.Assets;
import com.gdxjam.ai.Messages;
import com.gdxjam.components.CommanderComponent;
import com.gdxjam.components.Components;
import com.gdxjam.systems.CameraSystem;

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
        super(new StretchViewport(16*50,9*50));
        this.engine = engine;
        commanderButtonMap = new HashMap<Entity,Actor>();

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        table = new Table(skin);

        final VerticalGroup commanderGroup = new VerticalGroup();

        final TextButton newCommanderButton = new TextButton("new", skin);
        newCommanderButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        commanderGroup.addActor(newCommanderButton);

        engine.addEntityListener(Family.all(CommanderComponent.class).get(), new EntityListener() {
            @Override
            public void entityAdded(final Entity entity) {
                TextButton textButton = new TextButton("COMMANDER" + (commanderGroup.getChildren().size +""), skin);
                textButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        engine.getSystem(CameraSystem.class).cameraController.goToSmooth(Components.STEERABLE_BODY.get(entity).getPosition());

                        if (getTapCount() > 1) {
                            engine.getSystem(CameraSystem.class).cameraController.smoothFollow(Components.STEERABLE_BODY.get(entity).getPosition());
                        }
                        HorizontalGroup horizontalGroup = new HorizontalGroup();
                        Label numberOfUnitsLabel = new Label("Units: " + Components.COMMANDER.get(entity).units.size, skin);
                        horizontalGroup.addActor(numberOfUnitsLabel);

                        TextButton regroupB = new TextButton("Regroup", skin);
                        regroupB.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                for(Entity e : Components.COMMANDER.get(entity).units) {
                                    MessageManager.getInstance().dispatchMessage(0f, Components.COMMANDER.get(entity), Components.STATE_MACHINE.get(e), Messages.REGROUP_ORDER);
                                }
                            }
                        });
                        horizontalGroup.addActor(regroupB);

                        TextButton resourcesB = new TextButton("Resources", skin);
                        horizontalGroup.addActor(resourcesB);

                        TextButton addUnitB = new TextButton("Add Unit", skin);
                        horizontalGroup.addActor(addUnitB);

                        horizontalGroup.setFillParent(true);
                        horizontalGroup.bottom();
                        GUItest.this.addActor(horizontalGroup);
                    }
                });
                commanderButtonMap.put(entity, textButton);
                commanderGroup.addActorBefore(newCommanderButton, textButton);

            }

            @Override
            public void entityRemoved(Entity entity) {
                commanderGroup.removeActor(commanderButtonMap.get(entity));
                commanderButtonMap.remove(entity);
            }
        });



        commanderGroup.setFillParent(true);
        commanderGroup.right();
        this.addActor(commanderGroup);
    }


}
