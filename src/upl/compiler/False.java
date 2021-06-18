package upl.compiler;

class False extends Expr {
	public False() {

	}

	public void display(String indent) {
		System.out.println(indent + "false");
	}

	public Value interpret(SymbolTable t) {
		return new BoolValue(false);
	}
}
