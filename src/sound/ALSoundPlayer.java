package sound;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

import core.AbstractResource;
import core.iGameResource;

public class ALSoundPlayer extends AbstractResource{

	public ALSoundPlayer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		super.init();
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
	public void cleanup() {
		AL.destroy();
	}

}
