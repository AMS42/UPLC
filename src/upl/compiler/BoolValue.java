package upl.compiler;

class BoolValue extends Value {

	public BoolValue(boolean b) {
		boolValue = b;
	}

	public boolean boolValue() {
		return boolValue;
	}

	public int intValue() {
		return boolValue ? 1 : 0;
	}

	public float floatValue() {
		return (float) (boolValue ? 1 : 0);
	}

	public String stringValue() {
		return Boolean.toString(boolValue);
	}

	public Value[] listValue() {
		return new Value[] { new BoolValue(Boolean.valueOf(boolValue)) };
	}

	public Object getValue() {
		return boolValue;
	}

	protected boolean boolValue;
}
