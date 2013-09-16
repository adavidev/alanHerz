package game.core.l3d;

import static org.lwjgl.opengl.GL11.glNormal3f;

import org.lwjgl.opengl.GL11;

import game.core.extensions.VectorX;

public class Quad {

	public Vertex[] vertices;
	private VectorX normal;
	
	public Quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4, VectorX normal)
	{
		vertices = new Vertex[4];
		vertices[0] = v1;
		vertices[1] = v2;
		vertices[2] = v3;
		vertices[3] = v4;
		
		this.normal = normal;
	}
	
	public void draw()
	{
		GL11.glBegin(GL11.GL_QUADS);                        // Draw Quads
		glNormal3f(normal.x, normal.y, normal.z);
		for (int i = 0; i < vertices.length; i++)
		{
			vertices[i].draw();
		}
		GL11.glEnd();   
	}

}
