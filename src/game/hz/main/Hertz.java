package game.hz.main;

import game.core.GameState;
import game.core.StateGame;

public class Hertz extends GameState {

	StateGame game;
	
	public Hertz() {
		game = new StateGame();
		game.setState(this);
		game.start();
	}
	
	public void init() {
		System.out.println("initializing");
	}
	
	public void update() {
		System.out.println("updating");
	}
	
	public void destroy() {
	}

}
