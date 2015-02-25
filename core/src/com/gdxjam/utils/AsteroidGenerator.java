package com.gdxjam.utils;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * 
 * @author Torin Wiebelt (Twiebs)
 *	Generates asteroids using OpenSimplexNoise created by Kurt Spencer
 */

public class AsteroidGenerator {
	
	private final OpenSimplexNoise noise;
	private final float vienSize;
	private final int octaves;
	private final float heightCutoff = -0.5f;
	private final float gain = 0.5f;
	

	public AsteroidGenerator(long seed, int octaves, float veinSize){
		noise = new OpenSimplexNoise(seed);
		this.octaves = octaves;
		this.vienSize = veinSize;
	}
	
	public float eval(int x, int y){
		float total = 0.0f;
		float frequency = (1f / vienSize);
		float amplitude = gain;
		for(int i = 0; i < octaves; i++){
			total += noise.eval(x * frequency, y * frequency) * amplitude;
			frequency *= 2.0f;
			amplitude *= gain;
		}
		
		return total;
	}
	
	
	public void scatterAsteroids(int width, int height, int rows, int cols, float scatering){
		float[][] heightMap = new float[width][height];
		float maxDistance = (float)Math.sqrt(Math.pow((width * 0.5), 2.0) + Math.pow((width * 0.5), 2.0));
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				float dX = Math.abs((width * 0.5f) - x);
				float dY = Math.abs((height * 0.5f) - y);
				float distance = (float)Math.sqrt((dX*dX) + (dY*dY));
				float value = eval(x, y);
				value -= (distance / maxDistance);
				heightMap[x][y] = value;
			}
		}
		

		
//		int rows = (int)(width * density);
//		int cols = (int)(height * density);
		float xDensity = width / rows;
		float yDensity = height / cols;
		for(int x = 0; x < rows + 1; x++){
			for(int y = 0; y < cols + 1; y++){
				if(heightMap[x][y] > heightCutoff){
					EntityFactory.createAsteroid(new Vector2(xDensity * x + MathUtils.random(-scatering, scatering) * xDensity,
						yDensity * y +  MathUtils.random(-scatering, scatering) * yDensity), 0.2f);
				}
			}

		}
		
	}
	
	public void generateAsteroidField(Vector2 center, float width, float height, float minRadius, float maxRadius, float density){
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
          EntityFactory.createAsteroid(new Vector2(c.x,c.y), c.radius);
      }
  }
	

}
