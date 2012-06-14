package formbuilder.variables;

import java.util.Map;

public class TextClinicalVariable extends ClinicalVariable {
	private int maxLength;

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	@Override
	public Map<String, Object> getMarshalMap() {
		Map<String, Object> res = super.getMarshalMap();
		res.put("maxLength", maxLength);
		return res;
	}
}
