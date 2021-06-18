package upl.compiler;

class IfElse extends Stmt {

	public IfElse(Expr expr, Block trueClause, Block falseClause) {
		this.expr = expr;
		this.trueClause = trueClause;
		this.falseClause = falseClause;
	}

	public Value interpret(SymbolTable t) {
		Value test = expr.interpret(t);
		if (test.boolValue()) {
			return trueClause.interpret(t);
		} else {
			return falseClause.interpret(t);
		}
	}

	public void display(String indent) {
		System.out.println(indent + "IfElse");
		expr.display(indent + "  ");
		trueClause.display(indent + "  ");
		falseClause.display(indent + "  ");
	}

	private Expr expr;
	private Block trueClause;
	private Block falseClause;
}
