package sound;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

import core.iGameResource;

public class ALSoundPlayer implements iGameResource {

	public ALSoundPlayer() {
		// TODO Auto-generated constructor stub
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
	public void cleanup() {
		AL.destroy();
	}

}
