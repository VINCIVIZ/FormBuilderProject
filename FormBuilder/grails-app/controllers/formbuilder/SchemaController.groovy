package formbuilder

import grails.converters.*;

class SchemaController {

	final String SCHEMA_DIR = System.getProperty( "user.home" ) + "/NLPSchemas/";

	//static allowedMethods = [save:'POST'];
		
    def save() {
		File dir = new File(SCHEMA_DIR);
		if (!dir.exists()) {
			dir.mkdir();
		} else {
			assert dir.isDirectory();
		}
		
		//def o = JSON.parse(params.content);
		
		
    	File file = new File(SCHEMA_DIR + params.schemaName);
		//file.write((o as XML).toString());
		file.write(params.content); 
		
		render 'OK';
	}
	
	def read() {
		File file = new File(SCHEMA_DIR + params.schemaName);
		render file.getText();		
	}
	
	def list() {
		def res = [];
		File dir = new File(SCHEMA_DIR); 
		if (dir.exists()) {
			dir.eachFile { file ->
				if (!file.name.equals(".DS_Store")) {
					res.add(name:file.name);
				}
			}
		}
		render res as JSON;
	}
}
