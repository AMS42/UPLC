package upl.compiler;

class Program extends ASTNode {

	public Program(Block block) {
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}

	public void interpret() {
		SymbolTable t = new SymbolTable();
		t.enter();
		block.interpret(t);
		t.exit();
	}

	public void display(String indent) {
		System.out.println(indent + "Program");
		block.display(indent + "  ");
	}

	private Block block;
}
