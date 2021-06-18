package upl.compiler;

import java.util.List;

class Global extends Stmt {

	public Global(List<String> globals) {
		this.globals = globals;
	}

	@Override
	public Value interpret(SymbolTable t) {
		for (int i = 0; i < globals.size(); i++) {
			t.globalBind(globals.get(i));
		}
		return new NoneValue();
	}

	@Override
	public void display(String indent) {
		System.out.println(indent + "Globals");
		for (int i = 0; i < globals.size(); i++) {
			System.out.println(indent + "  " + globals.get(i));
		}
	}
	
	public List<String> globals;
}
