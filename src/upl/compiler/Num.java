package upl.compiler;

class Num extends Expr {

	public Num(Object value, boolean isInteger) {
		String v = String.valueOf(value);
		if (isInteger) {
			i = Integer.parseInt(v.substring(0, v.length() - 2));
			f = null;
		} else {
			f = Float.parseFloat(v);
			i = null;
		}
	}

	public Object getValue() {
		return f == null ? i : f;
	}

	public Value interpret(SymbolTable t) {
		if (f == null) {
			return new IntValue(i);
		} else {
			return new FloatValue(f);
		}
	}

	public void display(String indent) {
		System.out.println(indent + "Num " + (f == null ? i : f));
	}

	private Integer i;
	private Float f;
}
