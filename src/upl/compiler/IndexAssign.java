package upl.compiler;

import upl.exceptions.OutOfBoundsError;

class IndexAssign extends Stmt {

	public IndexAssign(Id id, Expr index, Expr value) {
		this.id = id;
		this.index = index;
		this.value = value;
	}
	
	public void display(String indent) {
		System.out.println(indent + "IndexAssign " + id);
		index.display(indent + "  ");
		value.display(indent + "  ");
	}
	
	public Value interpret(SymbolTable t) {
		Object[] list = id.interpret(t).listValue();
		int ind = index.interpret(t).intValue();
		Value val = value.interpret(t);
		try {
			
			list[ind] = val;
			return val;
		} catch (ArrayIndexOutOfBoundsException e) {
			// System.err.println("Index " + ind + " is out of bounds for length " + list.length);
			// System.exit(1);
			// return null;
			throw new OutOfBoundsError("Index " + ind + " is out of bounds for length " + list.length);
		}
	}
	
	private Id id;
	private Expr index, value;

}
