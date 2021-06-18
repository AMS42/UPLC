package upl.compiler;

import upl.exceptions.OutOfBoundsError;
import upl.exceptions.TypeError;

class Random extends Expr {
	public Random(Expr lowerBound, Expr upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}


	@Override
	public Value interpret(SymbolTable t) {
		java.util.Random r = new java.util.Random();
		Value lowerVal = lowerBound == null ? new IntValue(0) : lowerBound.interpret(t);
		Value upperVal = upperBound.interpret(t);
		if (!(lowerVal instanceof IntValue || lowerVal instanceof FloatValue)) {
			throw new TypeError("Lowerbound must be a number");
		}
		if (!(upperVal instanceof IntValue || upperVal instanceof FloatValue)) {
			throw new TypeError("Upperbound must be a number");
		}
		if (lowerVal.floatValue() >= upperVal.floatValue()) {
			throw new OutOfBoundsError("The lower bound must not be greater than or equal to the upper bound");
		}
		if (lowerVal instanceof FloatValue || upperVal instanceof FloatValue) {
			return new FloatValue(r.nextFloat() * (upperVal.floatValue() - lowerVal.floatValue()) + lowerVal.floatValue());
		} else {
			return new IntValue(r.nextInt(upperVal.intValue() - lowerVal.intValue()) + lowerVal.intValue());
		}
	}

	@Override
	public void display(String indent) {
		System.out.println("Random");
		lowerBound.display(indent + "  ");
		upperBound.display(indent + "  ");

	}

	private Expr lowerBound, upperBound;
}
