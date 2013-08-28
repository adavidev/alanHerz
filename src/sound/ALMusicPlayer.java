/**
 * 
 */
package sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.*;
import org.lwjgl.util.WaveData;

/**
 * @author Alan
 * 
 */
public class ALMusicPlayer extends ALSoundPlayer implements iMusicPlayer {

	int buffer;

	int source;

	/**
	 * 
	 */
	public ALMusicPlayer() {
		init();
	}

	@Override
	public void init() {
		super.init();
		// Loads the wave file from this class's package in your classpath
		WaveData waveFile = null;
		try {
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("data"
					+ File.separatorChar + "music" + File.separatorChar
					+ "Explosion4.wav")));

			buffer = alGenBuffers();

			alBufferData(buffer, waveFile.format, waveFile.data,
					waveFile.samplerate);
			waveFile.dispose();

			source = alGenSources();
			AL10.alSourcei(source, AL_BUFFER, buffer);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sound.MusicPlayer#playRandomSong()
	 */
	@Override
	public void playRandomSong() {
		alSourcePlay(source);

	}

}
