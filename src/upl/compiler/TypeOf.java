package upl.compiler;

class TypeOf extends Expr {

	public TypeOf(Expr expr) {
		this.expr = expr;
	}

	public Value interpret(SymbolTable t) {
		String type;
		Value v = expr.interpret(t);
		if (v instanceof BoolValue) {
			type = "bool";
		} else if (v instanceof FloatValue) {
			type = "float";
		} else if (v instanceof IntValue) {
			type = "int";
		} else if (v instanceof ListValue) {
			type = "list";
		} else if (v instanceof NoneValue) {
			type = "none";
		} else if (v instanceof StringValue) {
			type = "string";
		} else if (v instanceof FuncValue) {
			type = "func";
		} else {
			type = null;
		}
		return new StringValue("<" + type + ">");
	}

	public void display(String indent) {
		System.out.println(indent + "TypeOf ");
		display(indent + "  ");
	}

	private Expr expr;
}
