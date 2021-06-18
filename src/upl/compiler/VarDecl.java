package upl.compiler;

class VarDecl extends Stmt {

	public VarDecl(String id, Object value) {
		this.id = id;
		this.value = value;
		if (value instanceof Boolean) {
			this.type = Type.BOOLEAN;
		} else if (value instanceof Float || value instanceof Double) {
			this.type = Type.FLOAT;
		} else if (value instanceof Integer) {
			this.type = Type.INT;
		} else if (value instanceof String) {
			this.type = Type.STRING;
		} else if (value instanceof None) {
			this.type = Type.NONE;
		}
	}

	public String getId() {
		return id;
	}

	public Object getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}

	public void display(String indent) {
		System.out.println(indent + "Var " + id + " = " + value);
	}

	public Value interpret(SymbolTable t) {
		Value ret;
		if (type == Type.BOOLEAN) {
			ret = new BoolCell((boolean) value);
			t.bind(id, ret);
		} else if (type == Type.FLOAT) {
			ret = new FloatCell((float) value);
			t.bind(id, ret);
		} else if (type == Type.INT) {
			ret = new IntCell((int) value);
			t.bind(id, ret);
		} else if (type == Type.NONE) {
			ret = new NoneValue();
			t.bind(id, ret);
		} else if (type == Type.STRING) {
			ret = new StringCell((String) value);
			t.bind(id, ret);
		} else {
			ret = null;
		}
		return ret;
	}

	private String id;
	private Object value;
	private Type type;
}
