import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;


public class Main {

	static final String BASE_URI = "http://idash.ucsd.edu/nlp/NLPschema.owl";
	
	public static void main(String[] args) throws MalformedURLException {
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open("NLPSchema.owl");
		
		//URL url = new URL("http://idash.ucsd.edu/nlp/NLPschema.owl");
		//URLConnection urlConn = url.openConnection();
		
		
		
		OntClass eventClass = model.getOntClass(BASE_URI + "#Event");
		ExtendedIterator it = eventClass.listSubClasses();
		while(it.hasNext()) {
			OntClass subClass = (OntClass) it.next();
			System.out.println(subClass.getLocalName());
		}
	}

}
