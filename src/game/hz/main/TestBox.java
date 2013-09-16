package game.hz.main;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import game.core.extensions.VectorX;
import game.core.objects.HZGameObject;

public class TestBox extends HZGameObject {
	
	private VectorX position;

	public TestBox() {
		super();
	}

	@Override
	public void draw() {
		GL11.glColor3f(0.5f,0.5f,1.0f);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex3f(position.x, position.y, position.z);
		GL11.glEnd( );
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public double getFacing() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void init() {
		position = new VectorX(0, 3, 0);
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
