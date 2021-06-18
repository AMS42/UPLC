package upl.compiler;

class For extends Stmt {

	public For(Expr num, Block body) {
		this.num = num;
		this.body = body;
	}

	public Value interpret(SymbolTable t) {
		for (int i = 0; i < num.interpret(t).intValue(); i++) {
			body.interpret(t);
		}
		return new NoneValue();
	}

	public void display(String indent) {
		System.out.print(indent + "For ");
		num.display(indent);
		body.display(indent + "    ");
	}

	private Expr num;
	private Block body;
}
