package upl.compiler;

class Token {

	public Token(int line, int column, TokenType type, String lexeme) {
		this.line = line;
		this.column = column;
		this.type = type;
		this.lexeme = lexeme;
	}

	public String toString() {
		StringBuilder result = new StringBuilder(this.type.toString());
		if (this.lexeme != null) {
			result.append(" ").append(this.lexeme);
		}
		result.append(" ").append(this.line).append(':').append(this.column);
		return result.toString();
	}

	private final int line, column;
	final TokenType type;
	final String lexeme;
}
