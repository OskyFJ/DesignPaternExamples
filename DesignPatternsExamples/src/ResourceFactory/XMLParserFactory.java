package ResourceFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class XMLParserFactory implements ResourceFactory {

	DocumentBuilderFactory dbf;
	
	
	public XMLParserFactory() {
		super();
		dbf = DocumentBuilderFactory.newInstance();
	}

	
	/** Creates a new DocumentBuilder to add to the Pool.
	 * @see ResourceFactory#createResource()
	 */
	@Override
	public Object createResource() {
		try {
			return dbf.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			System.err.println(pce.getCause().getMessage());
			return null;
		}
	}

	/** check that a returned DocumentBuilder is valid and reset parameter to defaults
	 * @see ResourceFactory#validateResources(java.lang.Object)
	 */
	@Override
	public boolean validateResource(Object o) {
		if(!(o instanceof DocumentBuilder)) {
			return false;
		}
		
		//reset
		DocumentBuilder db = (DocumentBuilder) o;
		db.setEntityResolver(null);
		db.setErrorHandler(null);
		
		return true;
	}

}
