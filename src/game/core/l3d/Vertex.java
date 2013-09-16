package game.core.l3d;

import org.lwjgl.opengl.GL11;

import game.core.extensions.VectorX;

public class Vertex extends VectorX {

	public Vertex(float f, float g, float h) {
		super(f, g, h);
	}
	
public void draw() {
	GL11.glVertex3f(x, y,	 z);
}

}
