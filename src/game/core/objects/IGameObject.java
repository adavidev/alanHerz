package game.core.objects;

import org.lwjgl.util.vector.Vector3f;

import game.core.iGameResource;

public interface IGameObject extends iGameResource {

	public void draw();
	
	public Vector3f getPosition();

	public double getFacing();
	
}
