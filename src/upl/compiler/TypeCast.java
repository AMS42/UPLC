package upl.compiler;

class TypeCast extends Expr {

	public TypeCast(TokenType type, Expr expr) {
		this.type = type;
		this.expr = expr;
	}

	public Value interpret(SymbolTable t) {
		Value value = expr.interpret(t);
		if (type == TokenType.BOOL) {
			return new BoolValue(value.boolValue());
		} else if (type == TokenType.FLOAT) {
			return new FloatValue(value.floatValue());
		} else if (type == TokenType.INT) {
			return new IntValue(value.intValue());
		} else if (type == TokenType.STR) {
			return new StringValue(value.stringValue());
		} else if (type == TokenType.LIST) {
			return new ListValue(value.listValue());
		} else {
			return new NoneValue();
		}
	}

	public void display(String indent) {
		System.out.println(indent + "Type Cast (" + type + ")");
		expr.display(indent + "  ");
	}

	private TokenType type;
	private Expr expr;
}
