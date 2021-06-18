package upl.compiler;

class UnOp extends Expr {

	public UnOp(Op1 op, Expr expr) {
		this.op = op;
		this.expr = expr;
	}

	public Op1 getOp() {
		return op;
	}

	public Expr getExpr() {
		return expr;
	}

	public void display(String indent) {
		System.out.println(indent + "UnOp " + op);
		expr.display(indent + "  ");
	}

	public Value interpret(SymbolTable t) {
		Value value = expr.interpret(t);
		switch (op) {
		case NEG:
			if (value instanceof IntValue) {
				return new IntValue(-value.intValue());
			} else {
				return new FloatValue(-value.floatValue());
			}

		case NOT:
			return new BoolValue(!value.boolValue());

		default:
			return null;
		}
	}

	private Op1 op;
	private Expr expr;
}
