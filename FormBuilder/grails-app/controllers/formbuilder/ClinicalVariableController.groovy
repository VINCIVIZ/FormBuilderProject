package formbuilder

import java.io.InputStream;

import org.springframework.dao.DataIntegrityViolationException

import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.ontology.CardinalityRestriction;
import com.hp.hpl.jena.ontology.MaxCardinalityRestriction;
import com.hp.hpl.jena.ontology.OntClass
import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator
import formbuilder.fields.*;
import grails.converters.JSON


class ClinicalVariableController {

	final String OWL_BASE_URI = "http://idash.ucsd.edu/nlp/NLPschema.owl";
	final String OWL_FILE_URL = "http://localhost:8080/FormBuilder/data/NLPschema.owl";
	
	def test1() {
		HashMap<String, Object> a = new HashMap<String, Object>();
		a.put("name", 1);
		a.put("count", "2b");
		render a as JSON;
	}
	
	def json() {
		render OntologyParser.sharedParser().listEvents() as JSON;
	}
	
	def fields() {
		render OntologyParser.sharedParser().getFields(params.name) as JSON;
	}
}
