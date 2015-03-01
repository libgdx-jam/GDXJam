
package com.gdxjam.utils;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.GameManager;
import com.gdxjam.ai.Squad;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.systems.SquadSystem;

/** @author Torin Wiebelt (Twiebs) Generates world bounds Generates the game world by creating an asteroid field using fBm applied
 *         OpenSimplexNoise Populates the world with entities. */

public class WorldGenerator {

	private final OpenSimplexNoise noise;
	private Random rng;

	private int width;
	private int height;
	private float radius;
	private WorldGeneratorParameter param;

	public WorldGenerator (int width, int height, long seed) {
		this(width, height, seed, new WorldGeneratorParameter());
	}

	public WorldGenerator (int width, int height, long seed, WorldGeneratorParameter param) {
		this.width = width;
		this.height = height;
		radius = width * 0.5f;
		noise = new OpenSimplexNoise(seed);
		rng = new Random(seed);
		this.param = param;
	}

	public void generate () {
		createWorldBounds();
		generateAsteroidField();
		populateWorld();
	}

	public void createWorldBounds () {
		// TODO
	}

	public void populateWorld () {
		Vector2 center = new Vector2(width * 0.5f, height * 0.5f);
		EntityFactory.createMothership(center);
		
		float distance = 16.0f;

		float seperationAngle = 360.0f / (float)param.initalSquads;
		//float initalAngle = rng.nextInt(359);
		float initalAngle = 18.0f;
		
		for(int i = 0; i < param.initalSquads; i++){
			Vector2 angleVec = new Vector2(distance, 0.0f).setAngle(initalAngle + seperationAngle * i);
			Vector2 position = center.cpy().add(angleVec);
			createSquad(position);
		}
	}
	
	public void createSquad(Vector2 position){
		Squad squad = GameManager.getEngine().getSystem(SquadSystem.class).createSquad(position);
		int posX = (int)position.x;
		int posY = (int)position.y;
		for(int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++){
				Entity entity = EntityFactory.createUnit(new Vector2(posX + x, posY + y), Faction.Enemy);
				GameManager.getEngine().getSystem(SquadSystem.class).addMember(entity, squad);
			}
		}
	}

	public void generateAsteroidField () {
		float[][] heightMap = generateHeightMap();
		generateAsteroids(heightMap);
	}

	private float[][] generateHeightMap () {
		float[][] heightMap = new float[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				float value = eval(x, y);
				heightMap[x][y] = value;
			}
		}

		return heightMap;
	}

	/** Evaluates a x,y pair with fBm applied OpenSimplexNoise. Applies a radial mask to the value.
	 * @param x The x coord
	 * @param y The y coord
	 * @return The heightmap value */
	private float eval (int x, int y) {
		float total = 0.0f;
		float frequency = param.frequency;
		float amplitude = param.gain;
		for (int i = 0; i < param.octaves; i++) {
			total += noise.eval((float)x * frequency, (float)y * frequency) * amplitude;
			frequency *= param.lacunarity;
			amplitude *= param.gain;
		}

		// Apply a radial mask to the heightmap.
		// Lowers noise value as distance from center increases.

		float centerToX = x - radius;
		float centerToY = y - radius;
		float distanceToCenter = Math.abs((float)Math.sqrt(centerToX * centerToX + centerToY * centerToY));
		float distanceScalar = (distanceToCenter / radius);

		total -= distanceScalar;

		// Forces the center to be clear and the edges to be closed
// if(distanceScalar <= 0.45f ){
// total = 1;
// } else if(distanceToCenter / radius >= 0.9f){
// total -= distanceScalar;
// }

		total = MathUtils.clamp(total, -1.0f, 1.0f);
		return total;
	}

	/** Generates asteroids using natural scattering and values from the heightmap.
	 * @param heightMap */

	private void generateAsteroids (float[][] heightMap) {
		int totalRows = (int)((width - 1) * param.asteroidDensity);
		int totalCols = (int)((height - 1) * param.asteroidDensity);

		float rowSpacing = (float)width / (float)totalRows;
		float colSpacing = (float)height / (float)totalCols;

		for (int row = 0; row < totalRows; row++) {
			for (int col = 0; col < totalCols; col++) {
				float heightValue = heightMap[(int)(row * rowSpacing)][(int)(col * colSpacing)];
				if (heightValue <= param.heightThreshold) {
					Vector2 pos = new Vector2((row * rowSpacing)
						+ (randomSign() * param.asteroidScaling) * rowSpacing, (col * colSpacing)
						+ (randomSign() * param.asteroidScattering) * colSpacing);

					float radius = param.asteroidBaseRadius + rng.nextFloat() * param.asteroidScaling;
					if(rng.nextFloat() <= param.asteroidExtraScalingChance){
						radius += param.asteroidScaling * 2;
					}
					EntityFactory.createAsteroid(pos, radius);
				}
			}

		}
	}
	
	private float randomSign(){
		return rng.nextBoolean() ? 1 : -1;
	}

	public static class WorldGeneratorParameter {
		public int octaves = 12;
		public float lacunarity = 2.0f;
		public float frequency = 1.0f / 64.0f;
		public float gain = 1.0f / lacunarity;
		public float heightThreshold = -0.7f;

		public float asteroidDensity = 0.4f;
		public float asteroidScattering = 0.25f;
		public float asteroidScaling = 0.4f;
		public float asteroidExtraScalingChance = 0.02f;
		public float asteroidBaseRadius = 0.25f;

		public int initalSquads = 5;
		public int squadMembers = 9;
	}

}
