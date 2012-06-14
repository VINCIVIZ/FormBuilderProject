package formbuilder.variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplexClinicalVariable extends ClinicalVariable {
	private String typeName;
	private List<ClinicalVariable> members;
	private HashMap<String, String> enumValues;
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public List<ClinicalVariable> getMembers() {
		return members;
	}
	public void setMembers(List<ClinicalVariable> members) {
		this.members = members;
	}
	public HashMap<String, String> getEnumValues() {
		return enumValues;
	}
	public void setEnumValues(HashMap<String, String> enumValues) {
		this.enumValues = enumValues;
	}
	
	@Override
	public Map<String, Object> getMarshalMap() {
		Map<String, Object> res = super.getMarshalMap();
		res.put("typeName", typeName);
		List<Map<String, Object>> attrMap = new ArrayList<Map<String, Object>>();
		for (ClinicalVariable v : members) {
			attrMap.add(v.getMarshalMap());
		}
		res.put("members", attrMap);
		res.put("enumValues", enumValues);
		return res;
	}
}
