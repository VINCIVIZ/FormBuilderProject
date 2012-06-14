package formbuilder.variables;

import java.util.Map;

import formbuilder.Nullable;

public class IntegerClinicalVariable extends ClinicalVariable {
	private Nullable<Integer> minValue = new Nullable<Integer>();
	private Nullable<Integer> maxValue = new Nullable<Integer>();
	

	public Nullable<Integer> getMinValue() {
		return minValue;
	}

	public void setMinValue(Nullable<Integer> minValue) {
		this.minValue = minValue;
	}

	public Nullable<Integer> getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Nullable<Integer> maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public Map<String, Object> getMarshalMap() {
		Map<String, Object> res = super.getMarshalMap();
		res.put("minValue", minValue.getObject());
		res.put("maxValue", maxValue.getObject());
		return res;
	}
}
