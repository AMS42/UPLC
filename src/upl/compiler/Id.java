package upl.compiler;

class Id extends Expr {

	public Id(String id) {
		this.id = id;
	}

	public Value interpret(SymbolTable t) {
		return t.lookup(id);
	}

	public void display(String indent) {
		System.out.println(indent + "Id " + id);
	}

	private String id;
}
