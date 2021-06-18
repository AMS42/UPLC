package upl.compiler;

import java.util.Scanner;

class Prompt extends Expr {

	public Prompt(String message) {
		this.message = message;
	}

	public Value interpret(SymbolTable t) {
		Scanner sc = SymbolTable.sc;
		if (message.charAt(0) == '"') {
			message = message.substring(1);
		}
		if (message.charAt(message.length() - 1) == '"') {
			message = message.substring(0, message.length() - 1);
		}
		System.out.print(message);
		String ret = sc.nextLine();
		return new StringValue(ret);
	}

	public void display(String indent) {
		System.out.println(indent + "Prompt " + message);
	}

	private String message;
}
