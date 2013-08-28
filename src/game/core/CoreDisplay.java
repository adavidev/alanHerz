package game.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public abstract class CoreDisplay {

	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		init();

		while (!Display.isCloseRequested()) {

			update();

			Display.update();
		}

		destroy();
		Display.destroy();
	}

	protected abstract  void init();

	protected abstract  void update();
	
	protected abstract  void destroy();
}
