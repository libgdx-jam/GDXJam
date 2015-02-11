package com.gdxjam.map;

import com.gdxjam.tiles.Tile;

public interface Map {

	public String getKey();

	public void setKey(String key);

	public void add(Tile tile);

	public void remove(Tile tile);

	public Object getTiles();

	public void save(String key);

	public void load(String key);

}
