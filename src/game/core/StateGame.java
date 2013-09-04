package game.core;

public class StateGame extends GloballyManagedGame {

	private GameState state;

	public StateGame() {

	}

	public void setState(GameState state) {
		this.state = state;
	}

	public GameState getState() {
		return state;
	}

	protected void init() {
		super.init();
		state.init();
	}
	
	protected void update() {
		super.update();
		state.update();
	}
	
	protected void destroy() {
		super.destroy();
		state.destroy();
	}
}
