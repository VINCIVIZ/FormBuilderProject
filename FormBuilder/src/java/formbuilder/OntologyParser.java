package formbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.CardinalityRestriction;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.MaxCardinalityQRestriction;
import com.hp.hpl.jena.ontology.MaxCardinalityRestriction;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import formbuilder.variables.BooleanClinicalVariable;
import formbuilder.variables.ClinicalVariable;
import formbuilder.variables.ComplexClinicalVariable;
import formbuilder.variables.DateClinicalVariable;
import formbuilder.variables.DoubleClinicalVariable;
import formbuilder.variables.IntegerClinicalVariable;
import formbuilder.variables.TextClinicalVariable;
import formbuilder.variables.TimeClinicalVariable;

public class OntologyParser {
	private final String OWL_BASE_URI = "http://idash.ucsd.edu/nlp/NLPschema.owl";
	private final String OWL_FILE_URL = "http://localhost:8080/FormBuilder/data/NLPschema.owl";
	
	private static OntologyParser instance;
	
	private OntModel model;
	private AnnotationProperty displayLevelProp;
	private AnnotationProperty altLabelProp;
	private AnnotationProperty definitionProp;
	
	public static OntologyParser sharedParser() {
		if (instance == null) {
			instance = new OntologyParser();
		}
		
		return instance;
	} 
	
	private OntologyParser() {
		model = ModelFactory.createOntologyModel();	
		model.read(OWL_FILE_URL, OWL_BASE_URI, null);
		displayLevelProp = model.getAnnotationProperty(OWL_BASE_URI + "#displayLevel");
		altLabelProp = model.getAnnotationProperty(OWL_BASE_URI + "#altLabel");
		definitionProp = model.getAnnotationProperty(OWL_BASE_URI + "#definition");
	}
	
	List<String> listEvents() {
		ArrayList<String> res = new ArrayList<String>();
		
		OntModel model = ModelFactory.createOntologyModel();
		
		model.read(OWL_FILE_URL, OWL_BASE_URI, null);
		
		OntClass eventClass = model.getOntClass(OWL_BASE_URI + "#Event");
		ExtendedIterator it = eventClass.listSubClasses();
		while(it.hasNext()) {
			OntClass subClass = (OntClass) it.next();
			String name = subClass.getLocalName();
			res.add(name);
		}
		
		return res;
	}
	
	List<ClinicalVariable> getFields(String typeName) {
		List<ClinicalVariable> res = new ArrayList<ClinicalVariable>();
				
		OntClass subClass = model.getOntClass(OWL_BASE_URI + "#" + typeName);
		
		//get all members from "superclasses"...
		ExtendedIterator it = subClass.listSuperClasses();
		while (it.hasNext()) {
			OntClass superClass = (OntClass) it.next();
			if (!superClass.isRestriction()) {
				continue;
			}
						
			Restriction restriction = superClass.asRestriction();
			
			OntProperty property = restriction.getOnProperty();
			String propertyName = property.getLocalName();
			ClinicalVariable var;
			
			if (restriction.isSomeValuesFromRestriction()) {
				SomeValuesFromRestriction r = restriction.asSomeValuesFromRestriction();
				var = getType(propertyName, r.getSomeValuesFrom().getLocalName());
				var.setOptional(true);
			} else if (restriction.isMaxCardinalityRestriction()) {
				MaxCardinalityRestriction r = restriction.asMaxCardinalityRestriction();
				var = getType(propertyName);
				var.setOptional(true);
				var.setMultiple(r.getMaxCardinality() > 1);
			} else if (restriction.isCardinalityRestriction()) {
				CardinalityRestriction r = restriction.asCardinalityRestriction();	
				if (r.getCardinality() != 1) {
					throw new AssertionError();
				}
				var = getType(propertyName);
				var.setOptional(false);
				var.setMultiple(false);
			} else {
				throw new AssertionError();
			}
			
			var.setName(propertyName);
			
			res.add(var);
		}
		
		return res;
	}
	
	private ClinicalVariable getType(String propertyName) {
		return getType(propertyName, null);
	}
	
	private ClinicalVariable getType(String propertyName, String typeHint) {
		OntProperty prop = model.getOntProperty(OWL_BASE_URI + "#" + propertyName);
		ClinicalVariable res = null;
		
		if (prop == null) {
			res = new TextClinicalVariable();
			throw new AssertionError();
		} else if (prop.isDatatypeProperty()) {
			OntProperty p = prop.asDatatypeProperty();
			String type = p.getRange().getLocalName();
			
			if (type.equals("int")) {
				res = new IntegerClinicalVariable();
			} else if (type.equals("nonNegativeInteger")) {
				IntegerClinicalVariable v = new IntegerClinicalVariable();
				v.setMinValue(new Nullable<Integer>(0));
				res = v;
			} else if (type.equals("float")) {
				res = new DoubleClinicalVariable();
			} else if (type.equals("boolean")) {
				res = new BooleanClinicalVariable();
			} else if (type.equals("time")) {
				res = new TimeClinicalVariable();
			} else if (type.equals("date")) {
				res = new DateClinicalVariable();
			} else if (type.equals("string")) {
				res = new TextClinicalVariable();
			} else {
				res = new TextClinicalVariable();
				throw new AssertionError(type);
			}
		} else if (prop.isObjectProperty()) {
			ObjectProperty p = prop.asObjectProperty();
			
			ComplexClinicalVariable v = new ComplexClinicalVariable();
			OntResource range = p.getRange();
			v.setTypeName(range == null ? typeHint : range.getLocalName());
			v.setMembers(getFields(v.getTypeName()));
			v.setEnumValues(getEnumValues(v.getTypeName()));
			
			//read annotations
			if (v.getTypeName() != null) {
				OntClass clz = model.getOntClass(OWL_BASE_URI + "#" + v.getTypeName());
				RDFNode labelNode = clz.hasProperty(altLabelProp) ? clz.getPropertyValue(altLabelProp) : null;
				if (labelNode != null) {
					Literal literal = (Literal)labelNode.as(Literal.class);
					v.setCaption(literal.getString());
				}
				
				RDFNode defNode = clz.hasProperty(definitionProp) ? clz.getPropertyValue(definitionProp) : null;
				if (defNode != null) {
					Literal literal = (Literal)defNode.as(Literal.class);
					v.setDescription(literal.getString());
				}
				
			}
			
			if (prop.hasProperty(displayLevelProp)) {
				RDFNode defNode = prop.getPropertyValue(displayLevelProp);
				Literal literal = (Literal)defNode.as(Literal.class);
				v.setDisplayLevel(new Nullable<Integer>(literal.getInt()));
			}
			
			res = v;
			
		} else {
			res = new TextClinicalVariable();
			throw new AssertionError();
		}
		
		return res;
	}
	
	private HashMap<String, String> getEnumValues(String typeName) {
		OntClass clz = model.getOntClass(OWL_BASE_URI + "#" + typeName);
		ExtendedIterator it = model.listIndividuals(clz);
		if (it == null) {
			return null;
		}
		
		HashMap<String, String> res = new HashMap<String, String>();
		while(it.hasNext()) {
			Object obj = it.next();
			Individual individual = (Individual)obj;
			
			//read alt label
			if (individual.hasProperty(altLabelProp)) {
				RDFNode node = individual.getPropertyValue(altLabelProp);
				Literal literal = (Literal)node.as(Literal.class);
				res.put(individual.getLocalName(), literal.getString());
			} else {
				res.put(individual.getLocalName(), individual.getLocalName());
			}
		}
		
		return (res.size() == 0) ? null : res;	
		//FIXME: if <xxxValue> is defined in NamedIndividual, it is always unexpectedly contained in some classes. 
	}
}
