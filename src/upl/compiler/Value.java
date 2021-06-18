package upl.compiler;

abstract class Value {
	public abstract boolean boolValue();

	public abstract int intValue();

	public abstract float floatValue();

	public abstract String stringValue();

	public abstract Value[] listValue();

	public abstract Object getValue();
}
