package upl.compiler;

import upl.exceptions.TypeError;
import upl.exceptions.DivideByZeroError;

class BinOp extends Expr {

	public BinOp(Expr left, Op2 op, Expr right) {
		this.left = left;
		this.op = op;
		this.right = right;
	}

	public Expr getLeft() {
		return left;
	}

	public Op2 getOp() {
		return op;
	}

	public Expr getRight() {
		return right;
	}

	public void display(String indent) {
		System.out.println(indent + "BinOp " + op);
		left.display(indent + "  ");
		right.display(indent + "  ");
	}

	public Value interpret(SymbolTable t) {
		Value lhs = left.interpret(t);
		Value rhs = right.interpret(t);

		switch (op) {
		case AND:
			if (lhs.boolValue()) {
				return rhs;
			} else {
				return lhs;
			}

		case OR:
			if (lhs.boolValue()) {
				return lhs;
			} else {
				return rhs;
			}

		case EQ:  // REFACTOR (no need for type casts anymore) && USE EXCEPTIONS
			if (lhs instanceof StringValue) {
				if (rhs instanceof StringValue) {
					return new BoolValue(lhs.stringValue().equals(rhs.stringValue()));
				} else {
					return new BoolValue(false);
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				return new BoolValue(lhs instanceof NoneValue && rhs instanceof NoneValue);
			} else if (lhs instanceof ListValue || rhs instanceof ListValue) {
				if (!(lhs instanceof ListValue && rhs instanceof ListValue)) {
					return new BoolValue(false);
				}
				Value[] ls = lhs.listValue();
				Value[] rs = rhs.listValue();
				if (ls.length != rs.length) {
					return new BoolValue(false);
				}
				for (int i = 0; i < ls.length; i++) {
					if (ls[i] instanceof BoolValue && rs[i] instanceof BoolValue) {
						if (ls[i].boolValue() != rs[i].boolValue()) {
							return new BoolValue(false);
						}
					} else if (ls[i] instanceof FloatValue && rs[i] instanceof FloatValue) {
						if (ls[i].floatValue() != rs[i].floatValue()) {
							return new BoolValue(false);
						}
					} else if (ls[i] instanceof IntValue && rs[i] instanceof IntValue) {
						if (ls[i].intValue() != rs[i].intValue()) {
							return new BoolValue(false);
						}
					} else if (ls[i] instanceof StringValue && rs[i] instanceof StringValue) {
						if (!ls[i].stringValue().equals(rs[i].stringValue())) {
							return new BoolValue(false);
						}
					} else if (ls[i] instanceof NoneValue && rs[i] instanceof NoneValue) {
						if (!ls[i].getValue().equals(rs[i].getValue())) {
							return new BoolValue(false);
						}
					} else if (ls[i] instanceof ListValue && rs[i] instanceof ListValue) {
						/* Multi-Dimensional Lists
							if (ls[i].listValue().length != rs[i].listValue().length) {
								return new BoolValue(false);
							}
							for (int j = 0; j < ((ListValue) ls[i]).listValue().length; j++) {
								Value ltmp = ls[i].listValue()[j];
								Value rtmp = rs[i].listValue()[j];
								if (ltmp == rtmp) {
									System.out.println("I was false");
									return new BoolValue(false);
								}
							}
							System.out.println("I was true");
							return new BoolValue(true);
						*/
						// System.err.println("Unsupported binary operand type(s) for == with multi-dimensional lists");
						// System.exit(1);
						throw new TypeError("Unsupported binary operand type(s) for == with multi-dimensional lists");
					} else {
						return new BoolValue(false);
					}
				}
				return new BoolValue(true);
			} else {
				if (rhs instanceof StringValue) {
					return new BoolValue(false);
				} else {
					return new BoolValue(lhs.floatValue() == rhs.floatValue());
				}
			}

		case NE:
			if (lhs instanceof StringValue) {
				if (rhs instanceof StringValue) {
					return new BoolValue(!lhs.stringValue().equals(rhs.stringValue()));
				} else {
					return new BoolValue(true);
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				return new BoolValue(!(lhs instanceof NoneValue && rhs instanceof NoneValue));
			} else if (lhs instanceof ListValue || rhs instanceof ListValue) {
				if ((lhs instanceof ListValue && rhs instanceof ListValue)) {
					return new BoolValue(false);
				}
				Value[] ls = lhs.listValue();
				Value[] rs = rhs.listValue();
				if (ls.length != rs.length) {
					return new BoolValue(true);
				}
				for (int i = 0; i < ls.length; i++) {
					if (ls[i] instanceof BoolValue && rs[i] instanceof BoolValue) {
						if (ls[i].boolValue() != rs[i].boolValue()) {
							return new BoolValue(true);
						}
					} else if (ls[i] instanceof FloatValue && rs[i] instanceof FloatValue) {
						if (ls[i].floatValue() != rs[i].floatValue()) {
							return new BoolValue(true);
						}
					} else if (ls[i] instanceof IntValue && rs[i] instanceof IntValue) {
						if (ls[i].intValue() != rs[i].intValue()) {
							return new BoolValue(true);
						}
					} else if (ls[i] instanceof StringValue && rs[i] instanceof StringValue) {
						if (!ls[i].stringValue().equals(rs[i].stringValue())) {
							return new BoolValue(true);
						}
					} else if (ls[i] instanceof NoneValue && rs[i] instanceof NoneValue) {
						if (!ls[i].getValue().equals(rs[i].getValue())) {
							return new BoolValue(true);
						}
					} else if (ls[i] instanceof ListValue && rs[i] instanceof ListValue) {
						// System.err.println("Unsupported binary operand type(s) for != with multi-dimensional lists");
						// System.exit(1);
						throw new TypeError("Unsupported binary operand type(s) for != with multi-dimensional lists");
					} else {
						return new BoolValue(true);
					}
				}
				return new BoolValue(false);
			} else {
				if (rhs instanceof StringValue) {
					return new BoolValue(true);
				} else {
					return new BoolValue(lhs.floatValue() != rhs.floatValue());
				}
			}

		case LE:
			if (lhs instanceof StringValue) {
				if (rhs instanceof StringValue) {
					return new BoolValue(lhs.stringValue().compareTo(rhs.stringValue()) <= 0);
				} else {
					return new BoolValue(false);
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				return new BoolValue(
						(lhs instanceof NoneValue && rhs instanceof NoneValue) || (lhs instanceof NoneValue));
			} else {
				if (rhs instanceof StringValue) {
					return new BoolValue(false);
				} else {
					return new BoolValue(lhs.floatValue() <= rhs.floatValue());
				}
			}

		case LT:
			if (lhs instanceof StringValue) {
				if (rhs instanceof StringValue) {
					return new BoolValue(lhs.stringValue().compareTo(rhs.stringValue()) < 0);
				} else {
					return new BoolValue(false);
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				return new BoolValue(lhs instanceof NoneValue);
			} else {
				if (rhs instanceof StringValue) {
					return new BoolValue(false);
				} else {
					return new BoolValue(lhs.floatValue() < rhs.floatValue());
				}
			}

		case GE:
			if (lhs instanceof StringValue) {
				if (rhs instanceof StringValue) {
					return new BoolValue(lhs.stringValue().compareTo(rhs.stringValue()) >= 0);
				} else {
					return new BoolValue(false);
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				return new BoolValue(
						(lhs instanceof NoneValue && rhs instanceof NoneValue) || (rhs instanceof NoneValue));
			} else {
				if (rhs instanceof StringValue) {
					return new BoolValue(false);
				} else {
					return new BoolValue(lhs.floatValue() >= rhs.floatValue());
				}
			}

		case GT:
			if (lhs instanceof StringValue) {
				if (rhs instanceof StringValue) {
					return new BoolValue(lhs.stringValue().compareTo(rhs.stringValue()) > 0);
				} else {
					return new BoolValue(false);
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				return new BoolValue(rhs instanceof NoneValue);
			} else {
				if (rhs instanceof StringValue) {
					return new BoolValue(false);
				} else {
					return new BoolValue(lhs.floatValue() > rhs.floatValue());
				}
			}

		case PLUS:
			if (lhs instanceof StringValue) {
				return new StringValue(lhs.stringValue().concat(rhs.stringValue()));
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				// System.err.println("Unsupported binary operand type(s) for + with 'none' and 'none'");
				// System.exit(1);
				// return null;
				throw new TypeError("Unsupported binary operand type(s) for +: 'NONE'");
			} else if (lhs instanceof FloatValue || rhs instanceof FloatValue) {
				return new FloatValue(lhs.floatValue() + rhs.floatValue());
			} else if (lhs instanceof ListValue || rhs instanceof ListValue) {
				Value[] ret = new Value[(lhs instanceof ListValue ? lhs.listValue().length : 1)
						+ (rhs instanceof ListValue ? rhs.listValue().length : 1)];
				if (lhs instanceof ListValue) {
					for (int i = 0; i < lhs.listValue().length; i++) {
						ret[i] = lhs.listValue()[i];
					}
					if (rhs instanceof ListValue) {
						for (int i = 0; i < rhs.listValue().length; i++) {
							ret[i + lhs.listValue().length] = rhs.listValue()[i];
						}
					} else {
						ret[lhs.listValue().length] = rhs;
					}
				} else {
					ret[0] = lhs;
					for (int i = 0; i < rhs.listValue().length; i++) {
						ret[i + 1] = rhs.listValue()[i];
					}
				}
				return new ListValue(ret);
			} else {
				return new IntValue(lhs.intValue() + rhs.intValue());
			}

		case MINUS:
			if (lhs instanceof StringValue && rhs instanceof StringValue) {
				if (lhs.stringValue().contains(rhs.stringValue())) {
					return new StringValue(lhs.stringValue().replace(rhs.stringValue(), ""));
				}
				return new StringValue(lhs.stringValue());
			} else if (lhs instanceof StringValue || rhs instanceof StringValue) {
				if (lhs instanceof BoolValue) {
					throw new TypeError("Unsupported binary operand types(s) for -:'bool' and 'str'");
			
				} else if (rhs instanceof BoolValue) {
					throw new TypeError("Unsupported binary operand types(s) for -:'str' and 'bool'");
				} else if (lhs instanceof FloatValue) {
					throw new TypeError("Unsupported binary operand types(s) for -:'float' and 'str'");
				} else if (rhs instanceof FloatValue) {
					throw new TypeError("Unsupported binary operand types(s) for -:'str' and 'float'");
				} else if (lhs instanceof IntValue) {
					throw new TypeError("Unsupported binary operand types(s) for -:'int' and 'str'");
				} else if (rhs instanceof IntValue) {
					throw new TypeError("Unsupported binary operand types(s) for -:'str' and 'int'");
				} else if (lhs instanceof ListValue) {
					throw new TypeError("Unsupported binary operand types(s) for -:'list' and 'str'");
				} else if (rhs instanceof ListValue) {
					throw new TypeError("Unsupported binary operand types(s) for -:'str' and 'list'");
				} else if (lhs instanceof NoneValue) {
					throw new TypeError("Unsupported binary operand types(s) for -:'NONE' and 'str'");
				} else if (rhs instanceof NoneValue) {
					throw new TypeError("Unsupported binary operand types(s) for -:'str' and 'NONE'");
				}
			} else if (lhs instanceof ListValue) {
				if (rhs instanceof BoolValue) {
					int targetIndex = -1;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (!(lhs.listValue()[i] instanceof BoolValue)) {
							continue;
						}
						if (lhs.listValue()[i].boolValue() == rhs.boolValue()) {
							targetIndex = i;
							break;
						}
					}
					if (targetIndex == -1) {
						return lhs;
					}
					Value[] ret = new Value[lhs.listValue().length - 1];
					int retIndex = 0;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (i == targetIndex) {
							continue;
						}
						ret[retIndex++] = lhs.listValue()[i];
					}
					return new ListValue(ret);
				} else if (rhs instanceof FloatValue) {
					int targetIndex = -1;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (!(lhs.listValue()[i] instanceof FloatValue)) {
							continue;
						}
						if (lhs.listValue()[i].floatValue() == rhs.floatValue()) {
							targetIndex = i;
							break;
						}
					}
					if (targetIndex == -1) {
						return lhs;
					}
					Value[] ret = new Value[lhs.listValue().length - 1];
					int retIndex = 0;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (i == targetIndex) {
							continue;
						}
						ret[retIndex++] = lhs.listValue()[i];
					}
					return new ListValue(ret);
				} else if (rhs instanceof IntValue) {
					int targetIndex = -1;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (!(lhs.listValue()[i] instanceof IntValue)) {
							continue;
						}
						if (lhs.listValue()[i].intValue() == rhs.intValue()) {
							targetIndex = i;
							break;
						}
					}
					if (targetIndex == -1) {
						return lhs;
					}
					Value[] ret = new Value[lhs.listValue().length - 1];
					int retIndex = 0;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (i == targetIndex) {
							continue;
						}
						ret[retIndex++] = lhs.listValue()[i];
					}
					return new ListValue(ret);
				} else if (rhs instanceof StringValue) {
					int targetIndex = -1;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (!(lhs.listValue()[i] instanceof StringValue)) {
							continue;
						}
						if (lhs.listValue()[i].stringValue() == rhs.stringValue()) {
							targetIndex = i;
							break;
						}
					}
					if (targetIndex == -1) {
						return lhs;
					}
					Value[] ret = new Value[lhs.listValue().length - 1];
					int retIndex = 0;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (i == targetIndex) {
							continue;
						}
						ret[retIndex++] = lhs.listValue()[i];
					}
					return new ListValue(ret);
				} else if (rhs instanceof NoneValue) {
					int targetIndex = -1;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (!(lhs.listValue()[i] instanceof NoneValue)) {
							continue;
						}
						targetIndex = i;
						break;
					}
					if (targetIndex == -1) {
						return lhs;
					}
					Value[] ret = new Value[lhs.listValue().length - 1];
					int retIndex = 0;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (i == targetIndex) {
							continue;
						}
						ret[retIndex++] = lhs.listValue()[i];
					}
					return new ListValue(ret);
				} else if (rhs instanceof ListValue) {  
					int targetIndex = -1;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (!(lhs.listValue()[i] instanceof ListValue)) {
							continue;
						}
						if (lhs.listValue()[i].listValue().length != rhs.listValue().length) {
							continue;
						}
						for (int j = 0; j < lhs.listValue()[i].listValue().length; j++) {
							if (!lhs.listValue()[i].listValue()[j].equals(rhs.listValue()[j])) {
								continue;
							}
						}
						targetIndex = i;
						break;
					}
					if (targetIndex == -1) {
						return lhs;
					}
					Value[] ret = new Value[lhs.listValue().length - 1];
					int retIndex = 0;
					for (int i = 0; i < lhs.listValue().length; i++) {
						if (i == targetIndex) {
							continue;
						}
						ret[retIndex++] = lhs.listValue()[i];
					}
					return new ListValue(ret);
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				throw new TypeError("Unsupported binary operand type(s) for -: 'NONE'");
			} else if (lhs instanceof FloatValue || rhs instanceof FloatValue) {
				return new FloatValue(lhs.floatValue() - rhs.floatValue());
			} else {
				return new IntValue(lhs.intValue() - rhs.intValue());
			}

		case TIMES:
			if (lhs instanceof StringValue && rhs instanceof StringValue) {
				// System.err.println("Unsupported binary operand type(s) for *: 'str' and 'str'");
				// System.exit(1);
				// return null;
				throw new TypeError("Unsupported binary operand type(s) for *: 'str' and 'str'");
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				// System.err.println("Unsupported binary operand type(s) for *: 'none' and 'none'");
				// System.exit(1);
				// return null;
				throw new TypeError("Unsupported binary operand type(s) for *: 'NONE'");
			} else if (lhs instanceof ListValue && rhs instanceof ListValue) {
				throw new TypeError("Unsupported binary operand type(s) for *: 'list' and 'list'");
			} else if (lhs instanceof StringValue || rhs instanceof StringValue) {
				if (lhs instanceof FloatValue) {
					// System.err.println("Unsupported binary operand type(s) for *: 'float' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for *: 'float' and 'str'");
				} else if (rhs instanceof FloatValue) {
					// System.err.println("Unsupported binary operand type(s) for *: 'str' and 'float'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for *: 'str' and 'float'");
				} else if (lhs instanceof ListValue) { 
					throw new TypeError("Unsupported binary operand type(s) for *: 'list' and 'str'");
				} else if (rhs instanceof ListValue) {
					throw new TypeError("Unsupported binary operand type(s) for *: 'str' and 'list'");
				} else {
					StringBuilder ret = new StringBuilder();
					String string = lhs instanceof StringValue ? lhs.stringValue() : rhs.stringValue();
					int times = lhs instanceof IntValue || lhs instanceof BoolValue ? lhs.intValue() : rhs.intValue();
					for (int i = 0; i < times; i++) {
						ret.append(string);
					}
					return new StringValue(ret.toString());
				}
			} else if (lhs instanceof ListValue || rhs instanceof ListValue) {
				if (lhs instanceof FloatValue) {
					throw new TypeError("Unsupported binary operand type(s) for *: 'float' and 'list'");
				} else if (rhs instanceof FloatValue) {
					throw new TypeError("Unsupported binary operand type(s) for *: 'list' and 'float'");
				} else {
					Value[] list = lhs instanceof ListValue ? lhs.listValue() : rhs.listValue();
					int n = lhs instanceof IntValue || lhs instanceof BoolValue ? lhs.intValue() : rhs.intValue();
					Value[] ret = new Value[list.length * n];
					for (int i = 0; i < ret.length; i++) {
						ret[i] = list[i % list.length];
					}
				}
				// System.err.println("Unsupported binary operand type(s) for *: 'list' and 'list'");
				// System.exit(1);
				// return null;
				// throw new TypeError("Unsupported binary operand type(s) for *: 'list' and 'list'");
			} else if (lhs instanceof FloatValue || rhs instanceof FloatValue) {
				return new FloatValue(lhs.floatValue() * rhs.floatValue());
			} else {
				return new IntValue(lhs.intValue() * rhs.intValue());
			}

		case DIV:
			if (lhs instanceof StringValue || rhs instanceof StringValue) {
				if (lhs instanceof BoolValue) {
					// System.err.println("Unsupported binary operand type(s) for /: 'bool' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for /: 'bool' and 'str'");
				} else if (rhs instanceof BoolValue) {
					// System.err.println("Unsupported binary operand type(s) for /: 'str' and 'bool'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for /: 'str' and 'bool'");
				} else if (lhs instanceof IntValue) {
					// System.err.println("Unsupported binary operand type(s) for /: 'int' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for /: 'int' and 'str'");
				} else if (rhs instanceof IntValue) {
					// System.err.println("Unsupported binary operand type(s) for /: 'str' and 'int'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for /: 'str' and 'int'");
				} else if (lhs instanceof FloatValue) {
					// System.err.println("Unsupported binary operand type(s) for /: 'float' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for /: 'float' and 'str'");
				} else if (rhs instanceof FloatValue) {
					// System.err.println("Unsupported binary operand type(s) for /: 'str' and 'float'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for /: 'str' and 'float'");
				} else if (lhs instanceof ListValue) {
					// System.err.println("Unsupported binary operand type(s) for /: 'list' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for /: 'list' and 'str'");
				} else if (rhs instanceof ListValue) {
					// System.err.println("Unsupported binary operand type(s) for /: 'str' and 'list'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for /: 'str' and 'list'");
				} else {
					// System.err.println("Unsupported binary operand type(s) for /: 'str' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for /: 'str' and 'str'");
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				// System.err.println("Unsupported binary operand type(s) for /: 'none' and 'none'");
				// System.exit(1);
				// return null;
				throw new TypeError("Unsupported binary operand type(s) for /: 'none' and 'none'");
			} else if (lhs instanceof ListValue || rhs instanceof ListValue) {
				// System.err.println("Unsupported binary operand type(s) for /: 'list' and 'list'");
				// System.exit(1);
				// return null;
				throw new TypeError("Unsupported binary operand type(s) for /: 'list' and 'list'");
			} else if (lhs instanceof FloatValue || rhs instanceof FloatValue) {
				if (rhs.floatValue() == 0.0) {
					throw new DivideByZeroError("Division by zero");
				}
				return new FloatValue(lhs.floatValue() / rhs.floatValue());
			} else {
				if (rhs.intValue() == 0) {
					throw new DivideByZeroError("Division by zero");
				}
				return new FloatValue(lhs.floatValue() / rhs.floatValue());
			}

		case INTDIV:
			if (lhs instanceof StringValue || rhs instanceof StringValue) {
				if (lhs instanceof BoolValue) {
					// System.err.println("Unsupported binary operand type(s) for //: 'bool' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for //: 'bool' and 'str'");
				} else if (rhs instanceof BoolValue) {
					// System.err.println("Unsupported binary operand type(s) for //: 'str' and 'bool'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for //: 'str' and 'bool'");
				} else if (lhs instanceof IntValue) {
					// System.err.println("Unsupported binary operand type(s) for //: 'int' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for //: 'int' and 'str'");
				} else if (rhs instanceof IntValue) {
					// System.err.println("Unsupported binary operand type(s) for //: 'str' and 'int'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for //: 'str' and 'int'");
				} else if (lhs instanceof FloatValue) {
					// System.err.println("Unsupported binary operand type(s) for //: 'float' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for //: 'float' and 'str'");
				} else if (rhs instanceof FloatValue) {
					// System.err.println("Unsupported binary operand type(s) for //: 'str' and 'float'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for //: 'str' and 'float'");
				} else if (lhs instanceof ListValue) {
					// System.err.println("Unsupported binary operand type(s) for //: list and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for //: list and 'str'");
				} else if (rhs instanceof ListValue) {
					// System.err.println("Unsupported binary operand type(s) for //: 'str' and list");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for //: 'str' and list");
				} else {
					// System.err.println("Unsupported binary operand type(s) for //: 'str' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for //: 'str' and 'str'");
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				// System.err.println("Unsupported binary operand type(s) for //: 'none' and 'none'");
				// System.exit(1);
				// return null;
				throw new TypeError("Unsupported binary operand type(s) for //: 'NONE'");
			} else if (lhs instanceof ListValue || rhs instanceof ListValue) {
				// System.err.println("Unsupported binary operand type(s) for //: 'list' and 'list'");
				// System.exit(1);
				// return null;
				throw new TypeError("Unsupported binary operand type(s) for //: 'list'");
			} else if (lhs instanceof FloatValue || rhs instanceof FloatValue) {
				if (rhs.floatValue() == 0.0) {
					throw new DivideByZeroError("Division by zero");
				}
				return new IntValue((int) (lhs.floatValue() / rhs.floatValue()));
			} else {
				if (rhs.intValue() == 0) {
					throw new DivideByZeroError("Division by zero");
				}
				return new IntValue(lhs.intValue() / rhs.intValue());
			}

		case MOD:
			if (lhs instanceof StringValue || rhs instanceof StringValue) {
				if (lhs instanceof BoolValue) {
					// System.err.println("Unsupported binary operand type(s) for %: 'bool' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for %: 'bool' and 'str'");
				} else if (rhs instanceof BoolValue) {
					// System.err.println("Unsupported binary operand type(s) for %: 'str' and 'bool'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for %: 'str' and 'bool'");
				} else if (lhs instanceof IntValue) {
					// System.err.println("Unsupported binary operand type(s) for %: 'int' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for %: 'int' and 'str'");
				} else if (rhs instanceof IntValue) {
					// System.err.println("Unsupported binary operand type(s) for %: 'str' and 'int'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for %: 'str' and 'int'");
				} else if (lhs instanceof FloatValue) {
					// System.err.println("Unsupported binary operand type(s) for %: 'float' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for %: 'float' and 'str'");
				} else if (rhs instanceof FloatValue) {
					// System.err.println("Unsupported binary operand type(s) for %: 'str' and 'float'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for %: 'str' and 'float'");
				} else if (lhs instanceof ListValue) {
					// System.err.println("Unsupported binary operand type(s) for %: 'list' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for %: 'list' and 'str'");
				} else if (rhs instanceof ListValue) {
					// System.err.println("Unsupported binary operand type(s) for %: 'str' and 'list'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for %: 'str' and 'list'");
				} else {
					// System.err.println("Unsupported binary operand type(s) for %: 'str' and 'str'");
					// System.exit(1);
					// return null;
					throw new TypeError("Unsupported binary operand type(s) for %: 'str' and 'str'");
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				// System.err.println("Unsupported binary operand type(s) for %: 'none' and 'none'");
				// System.exit(1);
				// return null;
				throw new TypeError("Unsupported binary operand type(s) for %: 'NONE'");
			} else if (lhs instanceof ListValue || rhs instanceof ListValue) {
				// System.err.println("Unsupported binary operand type(s) for %: ''list'' and ''list''");
				// System.exit(1);
				// return null;
				throw new TypeError("Unsupported binary operand type(s) for %: 'list'");
			} else if (lhs instanceof FloatValue || rhs instanceof FloatValue) {
				if (rhs.floatValue() == 0.0) {
					throw new DivideByZeroError("Division by zero");
				}
				return new FloatValue(lhs.floatValue() % rhs.floatValue());
			} else {
				if (rhs.intValue() == 0) {
					throw new DivideByZeroError("Division by zero");
				}
				return new IntValue(lhs.intValue() % rhs.intValue());
			}
			
		case EXP:
			if (lhs instanceof StringValue || rhs instanceof StringValue) {
				if (lhs instanceof BoolValue) {
					throw new TypeError("Unsupported binary operand type(s) for **: 'bool' and 'str'");
				} else if (rhs instanceof BoolValue) {
					throw new TypeError("Unsupported binary operand type(s) for **: 'str' and 'bool'");
				} else if (lhs instanceof IntValue) {
					throw new TypeError("Unsupported binary operand type(s) for **: 'int' and 'str'");
				} else if (rhs instanceof IntValue) {
					throw new TypeError("Unsupported binary operand type(s) for **: 'str' and 'int'");
				} else if (lhs instanceof FloatValue) {
					throw new TypeError("Unsupported binary operand type(s) for **: 'float' and 'str'");
				} else if (rhs instanceof FloatValue) {
					throw new TypeError("Unsupported binary operand type(s) for **: 'str' and 'float'");
				} else if (lhs instanceof ListValue) {
					throw new TypeError("Unsupported binary operand type(s) for **: 'list' and 'str'");
				} else if (rhs instanceof ListValue) {
					throw new TypeError("Unsupported binary operand type(s) for **: 'str' and 'list'");
				} else {
					throw new TypeError("Unsupported binary operand type(s) for **: 'str' and 'str'");
				}
			} else if (lhs instanceof NoneValue || rhs instanceof NoneValue) {
				throw new TypeError("Unsupported binary operand type(s) for **: 'NONE'");
			} else if (lhs instanceof ListValue || rhs instanceof ListValue) {
				throw new TypeError("Unsupported binary operand type(s) for **: 'list'");
			} else if (lhs instanceof FloatValue || rhs instanceof FloatValue) {
				return new FloatValue((float) Math.pow(lhs.floatValue(), rhs.floatValue()));
			} else {
				return new IntValue((int) Math.pow(lhs.intValue(), rhs.intValue()));
			} 

		default:
			return null;
		}
	}

	private Expr left;
	private Op2 op;
	private Expr right;
}
