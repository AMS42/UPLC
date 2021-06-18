package upl.compiler;

class Assign extends Stmt {

	public Assign(String id, Expr expr) {
		this.id = id;
		this.expr = expr;
	}

	public void display(String indent) {
		System.out.println(indent + "Assign " + id);
		expr.display(indent + "  ");
	}

	public Value interpret(SymbolTable t) {
		Value lhs = t.lookup(id);
		Value rhs = expr.interpret(t);

		if (lhs instanceof BoolCell) {
			((BoolCell) lhs).set(rhs.boolValue());
		} else if (lhs instanceof IntCell) {
			((IntCell) lhs).set(rhs.intValue());
		} else if (lhs instanceof FloatCell) {
			((FloatCell) lhs).set(rhs.floatValue());
		} else if (lhs instanceof StringCell) {
			((StringCell) lhs).set(rhs.stringValue());
		} else {
			t.bind(id, rhs);
		}

		return rhs;
	}

	private String id;
	private Expr expr;
}
