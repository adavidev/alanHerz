package game.core;

import game.core.resource.iGameResource;

public class LocalGameTime implements iGameResource{

	static double lastTime = System.currentTimeMillis();

	public static double getDelta() {
		double delta = (System.currentTimeMillis() - lastTime)/1000.0;
//		lastTime = System.currentTimeMillis();
		return delta;
	}

	public static double getCurrentTime() {
		return System.currentTimeMillis();
	}

	public static double getPreviousTime() {
		return lastTime;
	}

	@Override
	public void init() {
		lastTime = System.currentTimeMillis();
	}

	@Override
	public void update() {
		lastTime = System.currentTimeMillis();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
