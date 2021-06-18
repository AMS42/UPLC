package upl.compiler;

import java.util.List;

class Print extends Stmt {

	public Print(List<Item> items) {
		this.items = items;
	}

	public Value interpret(SymbolTable t) {
		for (Item it : items) {
			if (it instanceof ExprItem) {
				Expr expr = ((ExprItem) it).getExpr();
				Value value = expr.interpret(t);
				Object v;
				if (value instanceof StringValue || value instanceof ListValue) {
					v = value.stringValue();
				} else {
					v = value.getValue();
				}
				System.out.print(v);
			} else if (it instanceof StringItem) {
				System.out.print(((StringItem) it).getMsg());
			} else {
				System.out.print(it);
			}
			System.out.print(" ");
		}
		System.out.println();
		return new NoneValue();
	}

	public void display(String indent) {
		System.out.println(indent + "Print");
		for (Item it : items) {
			it.display(indent + "  ");
		}
	}

	private List<Item> items;
}
