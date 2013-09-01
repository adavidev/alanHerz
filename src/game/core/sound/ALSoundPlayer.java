package game.core.sound;

import game.core.AbstractResource;
import game.core.iGameResource;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;


public class ALSoundPlayer extends AbstractResource{

	public ALSoundPlayer() {
		super();
	}

	@Override
	public void init() {
		// Initialize OpenAL and clear the error bit.
		try {
			AL.create();
		} catch (LWJGLException le) {
			le.printStackTrace();
			return;
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		AL.destroy();
	}

}
