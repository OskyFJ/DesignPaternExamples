package ResourceFactory;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class ResourcePool {

	// TODO: work with GENERICS!! Set<ConcreteResource>, List<ConcreteResource>

	private ResourceFactory factory;
	private int maxObjects;
	private int curObjects;
	private boolean quit; // false by default

	/**
	 * Recursos prestados. (Loaned Resources)
	 */
	private Set outResources;

	/**
	 * Recursos en espera. (Waiting Resources)
	 * 
	 */

	private List inResources;

	public ResourcePool(ResourceFactory factory, int maxObjects) {
		super();
		this.factory = factory;
		this.maxObjects = maxObjects;

		curObjects = 0;

		outResources = new HashSet(maxObjects);
		inResources = new LinkedList();
	}

	/**
	 * Se intenta usar un recurso creado y en espera, sinó se crea uno nuevo.
	 * Sinó se espera hasta obtener un recurso disponible.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized Object getResource() {

		while (!quit) {
			// first try to find an existing resource
			if (!inResources.isEmpty()) {
				Object o = inResources.remove(0);

				// if the resource is invalid, create a replacement
				if (!factory.validateResource(o))
					o = factory.createResource();

				outResources.add(o);
				return o;
			}

			// next, create a new resource if we haven't
			// reached the limit yet
			if (curObjects < maxObjects) {
				Object o = factory.createResource();
				outResources.add(o);
				curObjects++;

				return o;
			}

			// if no resoruces are available, wait until one is returned
			try {
				wait();
			} catch (Exception ex) {
			}

		}

		// the pool is destroyed
		return null;
	}

	// return a resource to the pool
	@SuppressWarnings("unchecked")
	public synchronized void returnResource(Object o) {

		// Something is wrong. Just give up.
		if (!outResources.remove(o))
			throw new IllegalStateException("Returned item not in pool");

		inResources.add(o);
		notify();
	}

	public synchronized void destroy() {
		quit = true;
		notifyAll();
	}

}
