package upl.compiler;

import java.util.List;

class Block extends ASTNode {

	public Block(List<Stmt> body) {
		this.body = body;
	}

	public List<Stmt> getBody() {
		return body;
	}

	public void display(String indent) {
		System.out.println(indent + "Block");
		for (Stmt s : body) {
			s.display(indent + "  ");
		}
	}

	public Value interpret(SymbolTable t) {
		Value ret = null;
		for (Stmt s : body) {
			ret = s.interpret(t);
		}
		return ret;
	}

	private List<Stmt> body;
}
