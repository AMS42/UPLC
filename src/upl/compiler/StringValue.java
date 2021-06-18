package upl.compiler;

import upl.exceptions.TypeError;

class StringValue extends Value {

	public StringValue(String s) {
		stringValue = s;
	}

	public boolean boolValue() {
		return stringValue.toLowerCase().equals("false") ? false : stringValue.length() > 0;
	}

	public int intValue() {
		try {
			return (int) Math.floor(floatValue());
		} catch (NumberFormatException nfe) {
			throw new TypeError("Cannot type cast '" + stringValue + "' to 'int'");
		}
	}

	public float floatValue() {
		try {
			return Float.parseFloat(stringValue);
		} catch (NumberFormatException nfe) {
			throw new TypeError("Cannot type cast '" + stringValue + "' to 'float'");
		}
	}

	public String stringValue() {
		return stringValue;
	}

	public Value[] listValue() {
		Value[] ret = new Value[stringValue.length()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new StringValue(Character.toString(stringValue.charAt(i)));
		}
		return ret;
	}

	public Object getValue() {
		return stringValue;
	}

	protected String stringValue;
}
