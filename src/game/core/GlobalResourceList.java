package game.core;

import java.util.ArrayList;

public class GlobalResourceList {

	public static ArrayList<iGameResource> gameResources = new ArrayList<iGameResource>();

	public static void init() {
		synchronized (gameResources) {
			for (iGameResource resource : gameResources) {
				resource.init();
			}
		}
	}

	public static void update() {
		synchronized (gameResources) {
			for (iGameResource resource : gameResources) {
				resource.update();
			}
		}
	}

	public static void destroy() {
		synchronized (gameResources) {
			for (iGameResource resource : gameResources) {
				resource.destroy();
			}
		}
	}

}
