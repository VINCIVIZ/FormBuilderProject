package formbuilder.variables;

import java.util.Map;

import formbuilder.Nullable;

public class DoubleClinicalVariable extends ClinicalVariable {
	private Nullable<Double> minValue = new Nullable<Double>();
	private Nullable<Double> maxValue = new Nullable<Double>();
		
	public Nullable<Double> getMinValue() {
		return minValue;
	}
	public void setMinValue(Nullable<Double> minValue) {
		this.minValue = minValue;
	}

	public Nullable<Double> getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Nullable<Double> maxValue) {
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
