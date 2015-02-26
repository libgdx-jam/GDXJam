package com.gdxjam.utils.generators;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.utils.EntityFactory;
import com.gdxjam.utils.OpenSimplexNoise;

/**
 * 
 * @author Torin Wiebelt (Twiebs)
 * Uses open simplex noise created by Kurt Spencer to generate an asteroid field
 */

public class WorldGenerator {
	
	private final OpenSimplexNoise noise;
	private final float vienSize;
	private final int octaves;
	private final float heightCutoff = -0.8f;
	private final float gain = 0.9f;
	private final float asteroidRadius = 0.25f;
	
	private int width;
	private int height;
	

	public WorldGenerator(long seed, int octaves, float veinSize, int width, int height){
		noise = new OpenSimplexNoise(seed);
		this.octaves = octaves;
		this.vienSize = veinSize;
		this.width = width;
		this.height = height;
	}
	
	public void generate(){
		createWorldBounds();
		generateAsteroidField();
		populateWorld();
	}
	
	public void createWorldBounds(){
		//TODO 
	}
	
	public void populateWorld(){
		EntityFactory.createOutpost(new Vector2(width * 0.5f, height * 0.5f));
		//TODO Create starting squads
	}
	
	public void generateAsteroidField(){
		float[][] heightMap = generateHeightMap();
		applyRadialMask(heightMap);
		generateAsteroids(0.35f, 0.5f, 0.25f, heightMap);
	}
	
	public float[][] generateHeightMap(){
		float[][] heightMap = new float[width][height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				
				float value = eval(x, y);
				heightMap[x][y] = value;
			}
		}
		
		return heightMap;
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
	
	public void applyRadialMask(float[][] heightMap){
		float radius = width * 0.5f;
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				
				float centerToX = x - radius;
				float centerToY = y - radius;	
				float distanceToCenter = Math.abs((float)Math.sqrt(centerToX * centerToX + centerToY * centerToY));
				float distanceScalar = (distanceToCenter / radius);
			
				float value = heightMap[x][y] - distanceScalar;
				
				//Forces the center to be clear and the edges to be closed
				if(distanceScalar <= 0.45f ){
					value = 1;
				} else if(distanceToCenter / radius >= 0.9f){
					value -= distanceScalar;
				}
				
				value = MathUtils.clamp(value, -1.0f, 1.0f);
				heightMap[x][y] = value;
			}
		}
	}
	
	public void generateAsteroids(float density, float scatering, float scaling, float[][] heightMap){
		int totalRows = (int)((width - 1) * density);
		int totalCols = (int)((height - 1) * density);
		
		float rowSpacing = (float) width / (float) totalRows;
		float colSpacing = (float) height / (float) totalCols;
		
		for(int row = 0; row < totalRows; row++){
			for(int col = 0; col < totalCols; col++){
				float heightValue = heightMap[(int) (row * rowSpacing)][(int) (col * colSpacing)];
				if(heightValue <=  heightCutoff){
					Vector2 pos = new Vector2(
						(row * rowSpacing) + (MathUtils.random(-scatering, scatering) * rowSpacing),
						(col * colSpacing) + (MathUtils.random(-scatering, scatering) * colSpacing));
					
					EntityFactory.createAsteroid(pos, asteroidRadius + MathUtils.random(0, scaling));
				}
			}

		}
		
	}

}
	
