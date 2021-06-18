package upl.compiler;

class ExprStmt extends Stmt {

	public ExprStmt(Expr expr) {
		this.expr = expr;
	}

	public void display(String indent) {
		System.out.println(indent + "ExprStmt");
		expr.display(indent + "  ");
	}

	public Value interpret(SymbolTable t) {
		return expr.interpret(t);
	}

	private Expr expr;
}
