package upl.compiler;

class List extends Expr {

	public List(Object[] list) {
		this.list = list;
	}

	public Value interpret(SymbolTable t) {
		Value[] values = new Value[list.length];
		int i = 0;
		for (Object o : list) {
			if (o instanceof Item) {
				Expr e = ((ExprItem) o).getExpr();
				values[i] = e.interpret(t);
			}
			i++;
		}
		return new ListValue(values);
	}

	public void display(String indent) {
		StringBuilder l = new StringBuilder("[");
		for (int i = 0; i < list.length; i++) {
			if (i == list.length - 1) {
				l.append(list[list.length - 1]);
				break;
			}
			l.append(list[i]).append(", ");
		}
		l.append("]");
		System.out.println(indent + "List " + l.toString());
	}

	private Object[] list;
}
