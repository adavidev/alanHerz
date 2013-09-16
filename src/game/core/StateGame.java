package game.core;

public class StateGame extends GloballyManagedGame {

	private iGameState state;

	public StateGame() {

	}

	public void setState(iGameState state) {
		this.state = state;
	}

	public iGameState getState() {
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
