package upl.compiler;

import java.util.List;
import upl.exceptions.TypeError;

class LambdaValue extends Value {

	public LambdaValue(List<Param> ps, Stmt s) {
		this.ps = ps;
		this.s = s;
	}

	public String toString() {
		StringBuilder ret = new StringBuilder("LambdaVal\n");
		ret.append("Parameters: ").append(ps.toString()).append('\n');
		return ret.toString();
	}

	public List<Param> getParams() {
		return ps;
	}

	public Stmt getStmt() {
		return s;
	}

	public boolean boolValue() {
		// System.err.println("Cannot get boolean from lambda value");
		// System.exit(1);
		error("bool");
		return false;
	}

	public int intValue() {
		// System.err.println("Cannot get integer from lambda value");
		// System.exit(1);
		error("'int'");
		return Integer.MIN_VALUE;
	}

	public float floatValue() {
		// System.err.println("Cannot get float from lambda value");
		// System.exit(1);
		error("'float'");
		return Float.MIN_VALUE;
	}

	public String stringValue() {
		// System.err.println("Cannot get string from lambda value");
		// System.exit(1);
		return "<lambda>";
	}

	public Value[] listValue() {
		// System.err.println("Cannot get string from lambda value");
		// System.exit(1);
		error("'list'");
		return new Value[0];
	}

	public Object getValue() {
		return new None();
	}
	
	private void error(String initType) {
		throw new TypeError("Cannot get " + initType + " from 'lambda'");
	}

	private List<Param> ps;
	private Stmt s;
}
