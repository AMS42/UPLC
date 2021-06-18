package upl.compiler;

import java.util.ArrayList;
import java.util.List;
import upl.exceptions.TypeError;

class Call extends Expr {

	public Call(String id, List<Expr> args) {
		this.id = id;
		this.args = args;
	}

	public void display(String indent) {
		System.out.println(indent + "Call " + id);
		for (Expr expr : args) {
			expr.display(indent + "  ");
		}
	}

	public Value interpret(SymbolTable t) {
		Value v = t.lookup(id);
		if (v instanceof NoneValue) {
			System.err.println("Couldn't find func " + id + "()");
			// System.exit(1);
			return null;
		}
		List<Param> params;
		Block block;
		List<Value> as = new ArrayList<Value>();

		if (v instanceof FuncValue) {
			FuncValue funcV = (FuncValue) v;

			params = funcV.getParams();
			block = funcV.getBlock();
		} else {
			LambdaValue lambdaV = (LambdaValue) v;

			params = lambdaV.getParams();
			List<Stmt> ss = new ArrayList<>();
			ss.add(lambdaV.getStmt());
			block = new Block(ss);
		}

		for (Expr e : args) {
			as.add(e.interpret(t));
		}
		t.enter();
		Value result = call(params, block, as, t);
		t.exit();

		return result;
	}

	private Value call(List<Param> params, Block block, List<Value> as, SymbolTable t) {
		if (params == null) {
			return block.interpret(t);
		}

		if (params.size() > as.size()) {
			// System.err.println("Invalid number of arguments to the function " + id);
			// System.exit(1);
			// return null;
			throw new TypeError(id + "() is missing " + (params.size() - as.size()) 
								+ " argument" + (params.size() - as.size() == 1 ? "" : "s"));
		} else if (params.size() < as.size()) {
			throw new TypeError(id + "() takes " + params.size() + " parameter" + (params.size() == 1 ? "" : "s") + " but " 
								+ as.size() + " argument" + (as.size() == 1 ? " was " : "s were ") + "given");
		} else {
			for (int i = 0; i < params.size(); i++) {
				t.bind(params.get(i).getId(), as.get(i));
			}
			return block.interpret(t);
		}
	}

	private String id;
	private List<Expr> args;
}
