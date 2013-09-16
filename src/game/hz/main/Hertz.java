package game.hz.main;

import game.core.DefaultOpenGL;
import game.core.LocalGameTime;
import game.core.StateGame;
import game.core.iGameState;
import game.core.hzsound.ALMusicPlayer;
import herzog3d.Game;

public class Hertz implements  iGameState {

	StateGame game;
	Game myGame;
	
	public Hertz() {
		gameSpecificCrap();
		
		game = new StateGame();
		game.setState(this);
		game.start();
	}
	
	private void gameSpecificCrap() {
		new DefaultOpenGL();
		new LocalGameTime();
		
		myGame = Game.getInstance();

		new ALMusicPlayer();
	}

	@Override
	public void init() {
		
		myGame.init();
	}

	@Override
	public void update() {
		System.out.println("updating");
		
		myGame.update();
	}

	@Override
	public void destroy() {
		myGame.destroy();
	}

}
