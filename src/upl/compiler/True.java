package upl.compiler;

class True extends Expr {
	public True() {

	}

	public void display(String indent) {
		System.out.println(indent + "true");
	}

	public Value interpret(SymbolTable t) {
		return new BoolValue(true);
	}

}
