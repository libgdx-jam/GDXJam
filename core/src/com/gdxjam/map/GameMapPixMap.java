package com.gdxjam.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.gdxjam.tiles.Tile;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.Constants.BLOCK_TYPE;

public class GameMapPixMap implements Map {

	private String key;
	public float size;

	Array<Tile> tiles = new Array<Tile>();

	public GameMapPixMap(String key) {
		this.key = key;
	}

	public GameMapPixMap() {
	}

	public void convertPixmap(String filename) {
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		int[][] map = new int[pixmap.getWidth()][pixmap.getHeight()];
		Tile tile = new Tile();

		for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
			for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);

				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					tile = new Tile(pixelX, pixelY);
					tile.addTileData(BLOCK_TYPE.EMPTY);
					tiles.add(tile);
				} else if (BLOCK_TYPE.FLOOR.sameColor(currentPixel)) {
					tile = new Tile(pixelX, pixelY);
					tile.addTileData(BLOCK_TYPE.FLOOR);
					tiles.add(tile);
				} else if (BLOCK_TYPE.POST1.sameColor(currentPixel)) {
					tile = new Tile(pixelX, pixelY);
					tile.addTileData(BLOCK_TYPE.FLOOR);
					tile.addTileData(BLOCK_TYPE.POST1);
					tiles.add(tile);
				} else if (BLOCK_TYPE.POST2.sameColor(currentPixel)) {
					tile = new Tile(pixelX, pixelY);
					tile.addTileData(BLOCK_TYPE.FLOOR);
					tile.addTileData(BLOCK_TYPE.POST2);
					tiles.add(tile);
				} else {
					tile = new Tile(pixelX, pixelY);
					tile.addTileData(BLOCK_TYPE.EMPTY);
					tiles.add(tile);
					Gdx.app.error("GameMapPixMap", "Unidentified color "
							+ pixmap.getPixel(pixelX, pixelY) + " found at "
							+ pixelX + "," + pixelY);
				}
			}
			size = (pixmap.getWidth() > pixmap.getHeight()) ? pixmap.getWidth()
					: pixmap.getHeight();
		}

	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void add(Tile tile) {
		tiles.add(tile);
	}

	@Override
	public void remove(Tile tile) {
		tiles.removeValue(tile, true);
	}

	@Override
	public Array<Tile> getTiles() {
		return tiles;
	}

	private void loadPixMap(String filename) {
		convertPixmap(filename);
	}

	@Override
	public void save(String key) {
		Json json = new Json();
		// System.out.println(json.prettyPrint(map));
		FileHandle file = Gdx.files.local("maps/" + key + ".json");
		file.writeString(json.prettyPrint(this), false);
	}

	@Override
	public void load(String key) {
		setKey(key);
		FileHandle file = Gdx.files.local("maps/" + key + ".json");
		String fileString = file.readString();
		Json json = new Json();

		this.tiles = json.fromJson(GameMapPixMap.class, fileString).tiles;

		System.out.println("Loading " + file.file().getAbsolutePath());
		System.out.println(json.prettyPrint(json.fromJson(GameMapPixMap.class,
				fileString)));

	}

	public float getSize() {
		return size;
	}

}
