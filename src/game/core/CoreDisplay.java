package game.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public abstract class CoreDisplay {

	LocalGameTime theTime = new LocalGameTime();
	
	public void start() {
		initInternal();

		while (!Display.isCloseRequested()) {
			
			try {
	            Thread.sleep(10);
		    } catch (InterruptedException ie){}

			updateInternal();
		}

		destroyInternal();
	}
	
	private void initInternal()
	{
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		init();
		theTime.init();
	}
	
	private void updateInternal()
	{
		update();
		Display.update();
		theTime.update();
	}

	private void destroyInternal()
	{
		destroy();
		Display.destroy();
		theTime.destroy();
	}

	protected abstract  void init();

	protected abstract  void update();
	
	protected abstract  void destroy();
}
