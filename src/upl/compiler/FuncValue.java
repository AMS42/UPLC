package upl.compiler;

import java.util.List;
import upl.exceptions.TypeError;

class FuncValue extends Value {

	public FuncValue(List<Param> ps, Block block) {
		this.ps = ps;
		this.block = block;
	}

	public String toString() {
		StringBuilder ret = new StringBuilder("FuncVal\n");
		ret.append("Parameters: ").append(ps.toString()).append('\n');
		return ret.toString();
	}

	public List<Param> getParams() {
		return ps;
	}

	public Block getBlock() {
		return block;
	}

	public boolean boolValue() {
		// System.err.println("Cannot get boolean from function value");
		// System.exit(1);
		error("'bool'");
		return false;
	}

	public int intValue() {
		// System.err.println("Cannot get integer from function value");
		// System.exit(1);
		error("'int'");
		return Integer.MIN_VALUE;
	}

	public float floatValue() {
		// System.err.println("Cannot get float from function value");
		// System.exit(1);
		error("'float'");
		return Float.MIN_VALUE;
	}

	public String stringValue() {
		// System.err.println("Cannot get string from function value");
		// System.exit(1);
		return "<func>";
	}

	public Value[] listValue() {
		// System.err.println("Cannot get string from function value");
		// System.exit(1);
		error("'list'");
		return new Value[0];
	}

	public Object getValue() {
		return new None();
	}
	
	private void error(String initType) {
		throw new TypeError("Cannot get " + initType + " from 'func'");
	}

	private List<Param> ps;
	private Block block;
}
