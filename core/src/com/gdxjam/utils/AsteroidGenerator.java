package com.gdxjam.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author Torin Wiebelt (Twiebs)
 *	Generates asteroids using OpenSimplexNoise created by Kurt Spencer
 *	Derived from this technique.
 * http://gamedev.stackexchange.com/questions/54276/a-simple-method-to-create-island-map-mask
 */

public class AsteroidGenerator {
	
	private final OpenSimplexNoise noise;
	private final float vienSize;
	private final int octaves;
	private final float heightCutoff = 0.8f;
	private final float gain = 0.9f;
	
	private int width;
	private int height;
	

	public AsteroidGenerator(long seed, int octaves, float veinSize, int width, int height){
		noise = new OpenSimplexNoise(seed);
		this.octaves = octaves;
		this.vienSize = veinSize;
		this.width = width;
		this.height = height;
	}
	
//	public float eval(int x, int y){
//		float total = 0.0f;
//		float frequency = (1f / vienSize);
//		float amplitude = gain;
//		for(int i = 0; i < octaves; i++){
//			total += noise.eval(x * frequency, y * frequency) * amplitude;
//			frequency *= 2.0f;
//			amplitude *= gain;
//		}
//		
//		return total;
//	}
	
//	public float eval(int x, int y){
//		float dX = x - (width * 0.5f);
//		float dY = y - (height * 0.5f);
//		float distance = (float)(Math.sqrt((dX * dX) + (dY * dY)));
//		float modifier = distance / (width * 0.5f);
//		float value = (float)noise.eval(x / 128f, y / 128f);
//		value -= modifier;
//		value = MathUtils.clamp(value, -1.0f, 1.0f);
//		return value;
//	}
//	
	
	public void applyRadialMask(float[][] heightMap){
		float radius = width * 0.5f;
		float testBound = 25;
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				
				float centerToX = x - radius;
				float centerToY = y - radius;
				
				float distanceToCenter = Math.abs((float)Math.sqrt(centerToX * centerToX + centerToY * centerToY));
				
				if(distanceToCenter <= testBound){
					heightMap[x][y] = 0;
				} else{
					heightMap[x][y] = 1;
				}
//				float value = (distanceToCenter / radius);
//				heightMap[x][y] = value;
			}
		}
	}
	
	public void scatterAsteroids(int width, int height, int rows, int cols, float scatering){
		float[][] heightMap = new float[width][height];
//		for(int x = 0; x < width; x++){
//			for(int y = 0; y < height; y++){
//				
//				float value = (float)noise.eval(x / 16f, y / 16f);
//				//value = makeMask(x, y, value);
//				heightMap[x][y] = value;
//			}
//		}
		
		applyRadialMask(heightMap);
		testWorldSize(heightMap);
		
//		float xDensity = width / rows;
//		float yDensity = height / cols;
//		for(int x = 0; x < rows + 1; x++){
//			for(int y = 0; y < cols + 1; y++){
//				if(heightMap[x][y] >= heightCutoff){
//					EntityFactory.createAsteroid(new Vector2(xDensity * x + MathUtils.random(-scatering, scatering) * xDensity,
//						yDensity * y +  MathUtils.random(-scatering, scatering) * yDensity), 0.2f);
//				}
//			}
//
//		}
		
	}
	
	
	public void testWorldSize(float[][] heightMap){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				
				if(heightMap[x][y]  > 0.5f){
					EntityFactory.createAsteroid(new Vector2(x,  y), 0.25f);
				}
				
			}
		}
	}
	
//	private float getFactor(int value, int min, int max){
//		int full = max - min;
//		int part = value - min;
//		float factor = (float) part / (float) full;
//		return factor;
//	}
//	
//	
//	public int getDistanceToEdge(int x, int y){
//		int[] distances = new int[]{x, y, (width - x), (height - y) };
//		int min = distances[0];
//		for(int value : distances){
//			if(value < min){
//				min = value;
//			}
//		}
//		return min;
//	}
//	
//	public float makeMask(int x, int y, float oldValue){
//		int minValue = ( (height + width) / 2) / 100 * 2;
//		int maxValue = ( (height + width) / 2) / 100 / 20;
//		
//		if(getDistanceToEdge(x, y) <= minValue){
//			return 0;
//		}
//		else if(getDistanceToEdge(x, y) >= maxValue){
//			return 1;
//		}
//		else{
//			float factor = getFactor(getDistanceToEdge(x, y), minValue, maxValue);
//			return (oldValue + oldValue) * factor;
//		}
//	}
	
	
//	public void generateAsteroidField(Vector2 center, float width, float height, float minRadius, float maxRadius, float density){
//      //PROTOTYPE
//      //Density should be between 0 and 1
//      if(density<0 || density>1) density = 0.5f;
//
//      float amountOfTrees = Math.round(width*height*density/(2*minRadius*2*minRadius));
//      
//      Array<Circle> circles = new Array<Circle>();
//      for(int i = 0; i < amountOfTrees; i++){
//      	float treeRadius = MathUtils.random(minRadius, maxRadius);
//          Circle c = new Circle(center.cpy().add((new Vector2(width,height)).scl((float)Math.random(), (float)Math.random())).sub(width/2f, height/2f), treeRadius);
//          boolean intercept = false;
//          for(Circle cc : circles){
//              if(Intersector.overlaps(c,cc)){
//                  intercept = true;
//                  break;
//              }
//          }
//          if(!intercept){
//              circles.add(c);
//          }
//      }
//      for(Circle c : circles){
//          EntityFactory.createAsteroid(new Vector2(c.x,c.y), c.radius);
//      }
//  }
	

}
