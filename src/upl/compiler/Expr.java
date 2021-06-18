package upl.compiler;

abstract class Expr extends ASTNode {
	public abstract Value interpret(SymbolTable t);
}
