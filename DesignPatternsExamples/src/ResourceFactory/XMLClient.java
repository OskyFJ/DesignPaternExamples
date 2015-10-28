package ResourceFactory;
import javax.xml.parsers.DocumentBuilder;


public class XMLClient implements Runnable {

	private ResourcePool pool;
	
	public XMLClient(int poolSize) throws InterruptedException {
		pool = new ResourcePool(new XMLParserFactory(), poolSize);
		
		//...
		
		//start Threads, etc...
		Thread t = new Thread(this);
		t.start();
		
		// ...
		// wait for threads
		
		t.join();
		// cleanup
		pool.destroy();	
	}

	@Override
	public void run() {
		DocumentBuilder db;
		
		try {
			// get parser from pool
			db = (DocumentBuilder) pool.getResource();
		} catch(Exception e) {
			return;
		}
		
		try {
			//...
			// do parsing
			System.out.println("Do Parsing ...");
		} catch(Exception ex) {
			//...
		} finally {
//			make sure to always return resources we checkout
			pool.returnResource(db);
		}
	}
}
