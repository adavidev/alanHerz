package game.core;

import game.core.objects.IGameObject;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	public static final float JET_CHASE_DIST = 6;
	public static final float JET_CHASE_HEIGHT = 10;
	public static final float ROBOT_CHASE_DIST = 4;
	public static final float ROBOT_CHASE_HEIGHT = 6;

	protected static IGameObject target = null;
	public static Vector3f pos = new Vector3f(0, 0, 0f);

	public static void setTarget(IGameObject target) {
		Camera.target = target;
	}

	/**
	 * Update the camera position, based on the target mech.
	 * 
	 * 
	 */
	public static void update() {
		follow();
	}

	public static void follow() {
		Vector3f update = new Vector3f(target.getPosition());
		update.x -= JET_CHASE_DIST * (float) Math.cos(target.getFacing());
		update.y -= JET_CHASE_DIST * (float) Math.sin(target.getFacing());
		update.z += JET_CHASE_HEIGHT;

		update.x -= pos.x;
		update.y -= pos.y;
		update.z -= pos.z;

		pos.x += update.x;
		pos.y += update.y;
		pos.z += update.z;
	}

	public static void transform() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(40, 1, 1, 100);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		Vector3f targetPos = new Vector3f(target.getPosition());
		GLU.gluLookAt(pos.x, pos.y, pos.z, targetPos.x, targetPos.y,
				targetPos.z, 0, 0, 1);
	}

	/**
	 * Get the position of the camera;
	 * 
	 * @return
	 */
	public static Vector3f getPos() {
		return pos;
	}

	public static IGameObject getTarget() {
		return target;
	}
}
