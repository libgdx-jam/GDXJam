package com.gdxjam.utils.generators;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.EntityManager;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.OpenSimplexNoise;

public class WorldGenerator {
	
	private static PooledEngine engine;
	
	public static void setEngine(PooledEngine engine){
		WorldGenerator.engine = engine;
	}
	
	public static void generateForest(Vector2 center, float width, float height, float minRadius, float maxRadius, float density, String type){
      //PROTOTYPE
      //Density should be between 0 and 1
      if(density<0 || density>1) density = 0.5f;

      float amountOfTrees = Math.round(width*height*density/(2*minRadius*2*minRadius));
      
      Array<Circle> circles = new Array<Circle>();
      for(int i = 0; i < amountOfTrees; i++){
      	float treeRadius = MathUtils.random(minRadius, maxRadius);
          Circle c = new Circle(center.cpy().add((new Vector2(width,height)).scl((float)Math.random(), (float)Math.random())).sub(width/2f, height/2f), treeRadius);
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
          EntityFactory.createTree(new Vector2(c.x,c.y), c.radius, type);
      }
  }
	
		
}
	
