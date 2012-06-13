package formbuilder;

import com.hp.hpl.jena.ontology.MaxCardinalityRestriction
import com.hp.hpl.jena.ontology.OntClass
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty
import com.hp.hpl.jena.ontology.Restriction
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator

class OntologyParser {
	
	private final String OWL_BASE_URI = "http://idash.ucsd.edu/nlp/NLPschema.owl";
	private final String OWL_FILE_URL = "http://localhost:8080/FormBuilder/data/NLPschema.owl";
	
	private static OntologyParser instance;
	
	private OntModel model;
	
	static sharedParser() {
		if (instance == null) {
			instance = new OntologyParser();
		}
		
		return instance;
	} 
	
	private OntologyParser() {
		model = ModelFactory.createOntologyModel();	
		model.read(OWL_FILE_URL, OWL_BASE_URI, null);
	}
	
	List listEvents() {
		def res = [];
		
		OntModel model = ModelFactory.createOntologyModel();
		
		model.read(OWL_FILE_URL, OWL_BASE_URI, null);
		
		OntClass eventClass = model.getOntClass(OWL_BASE_URI + "#Event");
		ExtendedIterator<OntClass> it = eventClass.listSubClasses();
		while(it.hasNext()) {
			OntClass subClass = it.next();
			def name = subClass.getLocalName();
			HashMap item = new HashMap();
			item.name = name;
			res.add(item);
		}
		
		return res;
	}
	
	List getFields(String typeName) {
		def res = [];
		
		OntModel model = ModelFactory.createOntologyModel();
		model.read(OWL_FILE_URL, OWL_BASE_URI, null);
		
		OntClass subClass = model.getOntClass(OWL_BASE_URI + "#" + typeName);
		
		//get all members from "superclasses"...
		ExtendedIterator<OntClass> it = subClass.listSuperClasses();
		while (it.hasNext()) {
			def superClass = it.next();
			if (!superClass.isRestriction()) {
				continue;
			}
			
			HashMap item = new HashMap();
			
			Restriction restriction = superClass.asRestriction();
			
			OntProperty property = restriction.getOnProperty();
			def propName = property.getLocalName();
			item.name = propName;
			
			if (restriction.isSomeValuesFromRestriction()) {
				SomeValuesFromRestriction r = restriction.asSomeValuesFromRestriction();
				item.type = r.getSomeValuesFrom().getLocalName();
				item.optional = true;
				item.multiple = true;
				item.attributes = [];
				
				//read attributes	//TODO: recursion
				OntClass attrClass = model.getOntClass(OWL_BASE_URI + "#" + item.type);
				assert attrClass != null;
				ExtendedIterator<OntClass> attrIt = attrClass.listSuperClasses();
				while (attrIt.hasNext()) {
					OntClass attrSuperClass = attrIt.next();
					if (!attrSuperClass.isRestriction()) {
						continue;
					}
					Restriction attrRestriction = attrSuperClass.asRestriction();
					assert attrRestriction.isCardinalityRestriction();
					
					HashMap attrItem = new HashMap();
					attrItem.name = attrRestriction.getOnProperty().getLocalName();
					attrItem.type = "Literal";
					item.attributes.add(attrItem);
				}
			}
			else if (restriction.isMaxCardinalityRestriction()) {
				MaxCardinalityRestriction r = restriction.asMaxCardinalityRestriction();
				assert r.getMaxCardinality() == 1;
				
				
				
				item.type = "Literal";
				item.optional = true;
				item.multiple = false;	//TODO: max number
			}
			
			res.add(item);
		}
		
		return res;
	}
}
