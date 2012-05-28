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
import grails.converters.JSON


class ClinicalVariableController {
 
    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	final String OWL_BASE_URI = "http://idash.ucsd.edu/nlp/NLPschema.owl";
	final String OWL_FILE_URL = "http://localhost:8080/FormBuilder/data/NLPschema.owl";
	
	def json() {
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
		
		render res as JSON;
	}
	
	def fields() {
		def name = params.name;
		def res = [];
		
		OntModel model = ModelFactory.createOntologyModel();
		model.read(OWL_FILE_URL, OWL_BASE_URI, null);
		
		OntClass subClass = model.getOntClass(OWL_BASE_URI + "#" + name);
		
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
		 
		render res as JSON;
	}
	
    /*def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [clinicalVariableInstanceList: ClinicalVariable.list(params), clinicalVariableInstanceTotal: ClinicalVariable.count()]
    }

    def create() {
        [clinicalVariableInstance: new ClinicalVariable(params)]
    }

    def save() {
        def clinicalVariableInstance = new ClinicalVariable(params)
        if (!clinicalVariableInstance.save(flush: true)) {
            render(view: "create", model: [clinicalVariableInstance: clinicalVariableInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), clinicalVariableInstance.id])
        redirect(action: "show", id: clinicalVariableInstance.id)
    }

    def show() {
        def clinicalVariableInstance = ClinicalVariable.get(params.id)
        if (!clinicalVariableInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "list")
            return
        }

        [clinicalVariableInstance: clinicalVariableInstance]
    }

    def edit() {
        def clinicalVariableInstance = ClinicalVariable.get(params.id)
        if (!clinicalVariableInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "list")
            return
        }

        [clinicalVariableInstance: clinicalVariableInstance]
    }

    def update() {
        def clinicalVariableInstance = ClinicalVariable.get(params.id)
        if (!clinicalVariableInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (clinicalVariableInstance.version > version) {
                clinicalVariableInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'clinicalVariable.label', default: 'ClinicalVariable')] as Object[],
                          "Another user has updated this ClinicalVariable while you were editing")
                render(view: "edit", model: [clinicalVariableInstance: clinicalVariableInstance])
                return
            }
        }

        clinicalVariableInstance.properties = params

        if (!clinicalVariableInstance.save(flush: true)) {
            render(view: "edit", model: [clinicalVariableInstance: clinicalVariableInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), clinicalVariableInstance.id])
        redirect(action: "show", id: clinicalVariableInstance.id)
    }

    def delete() {
        def clinicalVariableInstance = ClinicalVariable.get(params.id)
        if (!clinicalVariableInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "list")
            return
        }

        try {
            clinicalVariableInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'clinicalVariable.label', default: 'ClinicalVariable'), params.id])
            redirect(action: "show", id: params.id)
        }
    }*/
}
