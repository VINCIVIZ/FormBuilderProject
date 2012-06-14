import formbuilder.fields.*;
import grails.converters.JSON
import formbuilder.Marshallable;

class BootStrap {

    def init = { servletContext -> 
		JSON.registerObjectMarshaller(Marshallable) {
			return it.getMarshalMap();
		}
    }
    def destroy = {
    }
}
