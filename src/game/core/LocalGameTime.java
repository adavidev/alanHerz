package game.core;

public class LocalGameTime {

	static double lastTime = System.currentTimeMillis();

	public static double getDelta() {
		double delta = (System.currentTimeMillis() - lastTime)/1000.0;
		lastTime = System.currentTimeMillis();
		return delta;
	}

	public static double getCurrentTime() {
		return System.currentTimeMillis();
	}

	public static double getPreviousTime() {
		return lastTime;
	}

}
