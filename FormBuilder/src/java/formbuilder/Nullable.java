package formbuilder;

public class Nullable<T> {
	private T value;
	private boolean hasValue;
	
	public Nullable(T value) {
		this.value = value;
		hasValue = true;
	}
	
	public Nullable() {
		hasValue = false;
	}
	
	public void setValue(T value) {
		this.value = value;
		hasValue = true;
	}
	
	public void clearValue() {
		hasValue = false;
	}
	
	public T getValue() {
		if (!hasValue) {
			throw new IllegalAccessError();
		}
		return value;
	}
	
	public boolean hasValue() {
		return hasValue;
	}
	
	public Object getObject() {
		return hasValue ? value : null;
	}
}
