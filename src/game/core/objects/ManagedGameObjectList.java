package game.core.objects;

import java.util.ArrayList;

import game.core.resource.AbstractResource;

public class ManagedGameObjectList extends AbstractResource {

	private static ManagedGameObjectList instance = null;
	
	ArrayList<IGameObject> gameObjects;

	public static ManagedGameObjectList getInstance() {
		if (instance == null)
		{
			instance = new ManagedGameObjectList();
		}
		return instance;
	}

	protected ManagedGameObjectList() {
		super();
		gameObjects = new ArrayList<IGameObject>();
	}

	public void init() {
	}

	public void update() {
		for (IGameObject resource : gameObjects) {
			resource.update();
			resource.draw();
		}
	}

	public void destroy() {
		for (IGameObject resource : gameObjects) {
			resource.destroy();
		}
	}
	
	public static void add(IGameObject go) {
		go.init();
		getInstance().gameObjects.add(go);
	}

}
