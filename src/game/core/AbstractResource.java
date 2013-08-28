package game.core;


public abstract class AbstractResource  implements iGameResource {

	public AbstractResource() {
		GlobalResourceList.gameResources.add(this);
	}
}
