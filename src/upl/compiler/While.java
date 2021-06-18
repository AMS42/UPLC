package upl.compiler;

class While extends Stmt {

	public While(Expr expr, Block body) {
		this.expr = expr;
		this.body = body;
	}

	public Value interpret(SymbolTable t) {
		Value test = expr.interpret(t);
		while (test.boolValue()) {
			body.interpret(t);
			test = expr.interpret(t);
		}
		return new NoneValue();
	}

	public void display(String indent) {
		System.out.println(indent + "While");
		expr.display(indent + "  ");
		body.display(indent + "  ");
	}

	private Expr expr;
	private Block body;
}
