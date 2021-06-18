package upl.compiler;

import java.util.List;

class LambdaDecl extends Expr {

	public LambdaDecl(List<Param> params, Stmt stmt) {
		this.params = params;
		this.stmt = stmt;
	}

	public Value interpret(SymbolTable t) {
		Value ret = new LambdaValue(params, stmt);
		return ret;
	}

	public void display(String indent) {
		System.out.println(indent + "Lambda");
		if (params != null) {
			for (Param p : params) {
				p.display(indent + "  ");
			}
		}
		stmt.display(indent + "  ");
	}

	private List<Param> params;
	private Stmt stmt;
}
