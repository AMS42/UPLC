package upl.compiler;

class FloatValue extends Value {

	public FloatValue(float f) {
		floatValue = f;
	}

	public boolean boolValue() {
		return floatValue > 0.0;
	}

	public int intValue() {
		return (int) Math.floor(floatValue);
	}

	public float floatValue() {
		return floatValue;
	}

	public String stringValue() {
		return Float.toString(floatValue);
	}

	public Value[] listValue() {
		return new Value[] { new FloatValue(Float.valueOf(floatValue)) };
	}

	public Object getValue() {
		return floatValue;
	}

	protected float floatValue;
}
