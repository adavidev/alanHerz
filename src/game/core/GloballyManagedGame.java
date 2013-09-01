package game.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import util.GLUtils;

public abstract class GloballyManagedGame extends CoreDisplay {

	public GloballyManagedGame() {
		// TODO Auto-generated constructor stub
	}
	
	protected void init() {
		GlobalResourceList.init();
	}
	
	protected void update() {
		GlobalResourceList.update();
	}
	
	protected void destroy() {
		GlobalResourceList.destroy();
	}
	
}
