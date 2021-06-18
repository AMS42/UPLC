package upl.compiler;

abstract class Stmt extends ASTNode {
	public abstract Value interpret(SymbolTable t);
}
