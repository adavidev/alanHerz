package game.hz.map;

import java.util.ArrayList;

import game.core.extensions.*;

public class TiledMap extends GameMap {

	private VectorX[][] grid;
	private ArrayList<Tile> tiles;
	
	public TiledMap() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw() {
		for(Tile tile : tiles) {
			tile.draw();
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
