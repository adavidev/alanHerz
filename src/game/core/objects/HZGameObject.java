package game.core.objects;

import util.Vector3f;
import herzog3d.HZObject;

public abstract class HZGameObject extends HZObject implements IGameObject {

	public HZGameObject() {
		super(new Vector3f(0, 0, 0));
		ManagedGameObjectList.add(this);
	}
}
