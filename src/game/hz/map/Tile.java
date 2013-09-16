package game.hz.map;

import game.core.extensions.VectorX;
import game.core.l3d.Quad;
import game.core.l3d.Vertex;

public class Tile extends Quad {

	public Tile(Vertex v1, Vertex v2, Vertex v3, Vertex v4, VectorX normal)
	{
		super(v1,v2,v3,v4,normal);
	}

}
