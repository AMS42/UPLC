package upl.compiler;

class If extends Stmt {

	public If(Expr expr, Block trueClause) {
		this.expr = expr;
		this.trueClause = trueClause;
	}

	public Value interpret(SymbolTable t) {
		Value test = (BoolValue) expr.interpret(t);
		if (test.boolValue()) {
			return trueClause.interpret(t);
		} else {
			return new NoneValue();
		}
	}

	public void display(String indent) {
		System.out.println(indent + "If");
		expr.display(indent + "  ");
		trueClause.display(indent + "  ");
	}

	private Expr expr;
	private Block trueClause;
}
