package game.core.extensions;

import org.lwjgl.util.vector.Vector3f;

public class VectorX extends Vector3f {

	public VectorX(float f, float g, float h) {
		super(f,g,h);
	}

	public static VectorX zero() {
		return new VectorX(0f,0f,0f);
	}

	public float distanceSquaredTo(VectorX v){
		return (x - v.getX())*(x - v.getX()) + 
			   (y - v.getY())*(y - v.getY()) + 
			   (z - v.getZ())*(z - v.getZ());
	}

	public float distanceTo(VectorX v){
		return (float)Math.sqrt(distanceSquaredTo(v));
	}
}
