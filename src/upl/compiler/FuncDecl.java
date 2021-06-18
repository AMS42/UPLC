package upl.compiler;

import java.util.List;

class FuncDecl extends Stmt {

	public FuncDecl(String id, List<Param> params, Block block) {
		this.id = id;
		this.params = params;
		this.block = block;
	}

	public Value interpret(SymbolTable t) {
		Value ret = new FuncValue(params, block);
		t.bind(id, ret);
		return ret;
	}

	public void display(String indent) {
		System.out.println(indent + "Func " + id);
		if (params != null) {
			for (Param p : params) {
				p.display(indent + "  ");
			}
		}
		block.display(indent + "  ");
	}

	private String id;
	private List<Param> params;
	private Block block;
}
