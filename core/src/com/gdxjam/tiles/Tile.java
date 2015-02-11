package com.gdxjam.tiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.utils.Constants.BLOCK_TYPE;

public class Tile {

	private Array<BLOCK_TYPE> tileData = new Array<BLOCK_TYPE>();

	private Vector2 position;

	public Tile() {
		position = new Vector2();
	}

	public Tile(Vector2 position) {
		this.position = position;
	}

	public Tile(float x, float y) {
		this.position = new Vector2(x, y);
	}

	public Array<BLOCK_TYPE> getTileData() {
		return tileData;
	}

	public void addTileData(BLOCK_TYPE type) {
		tileData.add(type);
	}

	public void clearTileData() {
		tileData.clear();
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public int getX() {
		return (int) position.x;
	}

	public int getY() {
		return (int) position.y;

	}

}
