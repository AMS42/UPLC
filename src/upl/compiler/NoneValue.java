package upl.compiler;

import upl.exceptions.TypeError;

class NoneValue extends Value {

	public NoneValue() {
		return;
	}

	public boolean boolValue() {
		// System.err.println("Cannot get a boolean value from none type");
		// System.exit(0);
		error("'bool'");
		return false;
	}

	public int intValue() {
		// System.err.println("Cannot get an integer value from none type");
		// System.exit(0);
		error("'int'");
		return Integer.MIN_VALUE;
	}

	public float floatValue() {
		// System.err.print("Cannot get a float value from none type");
		// System.exit(0);
		error("'float'");
		return Float.MIN_VALUE;
	}

	public String stringValue() {
		return "none";
	}

	public Value[] listValue() {
		return new Value[0];
	}

	public Object getValue() {
		return "NONE";
	}
	
	private void error(String initType) {
		throw new TypeError("Cannot get " + initType + " from 'NONE'");
	}

}
