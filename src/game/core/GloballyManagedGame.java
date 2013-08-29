package game.core;

public abstract class GloballyManagedGame extends CoreDisplay {

	public GloballyManagedGame() {
		// TODO Auto-generated constructor stub
	}

	protected void init() {
		GlobalResourceList.init();
	}

	protected void update() {
		GlobalResourceList.update();
	}
	
	protected void destroy() {
		GlobalResourceList.destroy();
	}
	
}
