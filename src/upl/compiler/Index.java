package upl.compiler;

import upl.exceptions.OutOfBoundsError;

public class Index extends Expr {

	public Index(Expr expr, Expr index) {
		this.expr = expr;
		this.index = index;
	}

	public Value interpret(SymbolTable t) {
		Object[] list = expr.interpret(t).listValue();
		int i = index.interpret(t).intValue();
		try {
			return (Value) list[i];
		} catch (ArrayIndexOutOfBoundsException e) {
			// System.err.println("Index " + i + " is out of bounds for length " + list.length);
			// System.exit(1);
			// return null;
			throw new OutOfBoundsError("Index " + i + " is out of bounds for length " + list.length);
		}
	}

	public void display(String indent) {
		System.out.println(indent + "Index");
		expr.display(indent + "  ");
		index.display(indent + "  ");

	}

	private Expr expr;
	private Expr index;
}
