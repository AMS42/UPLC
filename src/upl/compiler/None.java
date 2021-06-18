package upl.compiler;

class None extends Expr {

	public None() {

	}

	public void display(String indent) {
		System.out.println(indent + "none");
	}

	public Value interpret(SymbolTable t) {
		return new NoneValue();
	}

}
