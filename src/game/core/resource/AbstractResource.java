package game.core.resource;



public abstract class AbstractResource  implements iGameResource {

	public AbstractResource() {
		GlobalResourceList.gameResources.add(this);
	}
}
