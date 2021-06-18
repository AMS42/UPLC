package upl.compiler;

class Str extends Expr {

	public Str(String value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public Value interpret(SymbolTable t) {
		if (value.isEmpty()) {
			return new StringValue("");
		}
		if (value.charAt(0) == '"') {
			value = value.substring(1);
		}
		if (!value.isEmpty()) {
			if (value.charAt(value.length() - 1) == '"') {
				value = value.substring(0, value.length() - 1);
			}
		}
		return new StringValue(value);
	}

	public void display(String indent) {
		System.out.println(indent + "String '" + value + "'");
	}

	private String value;
}
