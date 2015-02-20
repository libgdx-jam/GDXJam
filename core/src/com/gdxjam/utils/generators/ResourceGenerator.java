package com.gdxjam.utils.generators;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.components.ResourceComponent;
import com.gdxjam.components.SteerableBodyComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.systems.PhysicsSystem;

/**
 * Created by SCAW on 17/02/2015.
 */
public class ResourceGenerator {

    public static void generate(PooledEngine engine, Vector2 position, float size, ResourceComponent.ResourceType resourceType){
        Entity entity = engine.createEntity();

        //Physics
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(position);
        def.linearDamping = 10.0f;
        Body body = engine.getSystem(PhysicsSystem.class).createBody(def);

        FixtureDef fixDef = new FixtureDef();
        fixDef.density = 1000000f; //very high so it doesn't move on collision
        CircleShape shape = new CircleShape();
        shape.setRadius(size);
        fixDef.shape = shape;

        body.createFixture(fixDef);
        shape.dispose();

        SteerableBodyComponent steerable = (SteerableBodyComponent) engine.createComponent(SteerableBodyComponent.class).init(body);
        entity.add(steerable);

        entity.add(engine.createComponent(SteeringBehaviorComponent.class));

        entity.add(engine.createComponent(ResourceComponent.class).init(resourceType));

        engine.addEntity(entity);

    }

    public static void generateForest(PooledEngine engine, Vector2 centerPosition, float width, float height, float angle, float density){
        //PROTOTYPE

        //Tree size
        float treeRadius = 0.10f;
        //Density should be between 0 and 1
        if(density<0 || density>1) density = 0.5f;

        float amountOfTrees = Math.round(width*height*density/(2*treeRadius*2*treeRadius));
        Array<Circle> circles = new Array<Circle>();
        for(int i = 0; i<amountOfTrees; i++){
            Circle c = new Circle(centerPosition.cpy().add((new Vector2(width,height)).scl((float)Math.random(), (float)Math.random())).sub(width/2f, height/2f), treeRadius);
            boolean intercept = false;
            for(Circle cc : circles){
                if(Intersector.overlaps(c,cc)){
                    intercept = true;
                    break;
                }
            }
            if(!intercept){
                circles.add(c);
            }
        }
        for(Circle c : circles){
            generate(engine, new Vector2(c.x,c.y), treeRadius, ResourceComponent.ResourceType.WOOD);
        }
    }

}
