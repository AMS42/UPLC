package upl.compiler;

class ListValue extends Value {

	public ListValue(Value[] listValues) {
		this.listValues = listValues;
	}

	public boolean boolValue() {
		return listValues.length > 0;
	}

	public int intValue() {
		return listValues.length;
	}

	public float floatValue() {
		return (float) listValues.length;
	}

	public String stringValue() {
		StringBuilder ret = new StringBuilder("[");
		for (int i = 0; i < listValues.length; i++) {
			if (listValues[i] instanceof Value) {
				if (i == listValues.length - 1) {
					ret.append(((Value) listValues[i]).stringValue());
					break;
				}
				ret.append(((Value) listValues[i]).stringValue()).append(", ");
			} else {
				if (i == listValues.length - 1) {
					ret.append(listValues[i]);
				} else {
					ret.append(listValues[i]).append(", ");
				}
			}
		}
		ret.append("]");
		return ret.toString();
	}

	public Value[] listValue() {
		return listValues;
	}

	public Object getValue() {
		return listValues;
	}

	protected Value[] listValues;
}
