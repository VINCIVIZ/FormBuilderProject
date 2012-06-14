package formbuilder.variables;

import java.util.HashMap;
import java.util.Map;

import formbuilder.Marshallable;
import formbuilder.Nullable;

public class ClinicalVariable implements Marshallable {
	private String name;
	private String caption;
	private String description;
	
	private boolean optional;
	private boolean multiple;
	
	private Nullable<Integer> displayLevel = new Nullable<Integer>();
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCaption() {
		return caption;
	}


	public void setCaption(String caption) {
		this.caption = caption;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public boolean isOptional() {
		return optional;
	}


	public void setOptional(boolean optional) {
		this.optional = optional;
	}


	public boolean isMultiple() {
		return multiple;
	}


	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	

	public Nullable<Integer> getDisplayLevel() {
		return displayLevel;
	}


	public void setDisplayLevel(Nullable<Integer> displayLevel) {
		this.displayLevel = displayLevel;
	}


	@Override
	public Map<String, Object> getMarshalMap() {
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("className", this.getClass().getSimpleName());
		res.put("fieldName", name);
		res.put("caption", caption);
		res.put("description", description);
		res.put("optional", optional);
		res.put("multiple", multiple);
		res.put("displayLevel", displayLevel.getObject());
		return res;
	}
	
	
}
