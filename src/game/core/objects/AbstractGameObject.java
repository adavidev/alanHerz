package game.core.objects;

public abstract class AbstractGameObject implements IGameObject {

	public AbstractGameObject() {
		ManagedGameObjectList.add(this);
	}
}
