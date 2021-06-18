package upl.compiler;

class IntValue extends Value {

	public IntValue(int i) {
		intValue = i;
	}

	public boolean boolValue() {
		return intValue > 0;
	}

	public int intValue() {
		return intValue;
	}

	public float floatValue() {
		return Float.valueOf(intValue);
	}

	public String stringValue() {
		return Integer.toString(intValue);
	}

	public Value[] listValue() {
		return new Value[] { new IntValue(Integer.valueOf(intValue)) };
	}

	public Object getValue() {
		return intValue;
	}

	protected int intValue;
}
