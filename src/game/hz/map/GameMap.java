package game.hz.map;

import game.core.extensions.VectorX;
import game.core.objects.AbstractGameObject;

public abstract class GameMap extends AbstractGameObject {

	@Override
	public VectorX getPosition() {
		return new VectorX(0,0,0);
	}

	@Override
	public double getFacing() {
		return 0;
	}
	
	public float getHeightAt(float x, float y) {
		return 0;
	}

}
