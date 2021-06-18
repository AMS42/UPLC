package upl.compiler;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import upl.exceptions.SyntaxError;

class Scanner {

	public Scanner(Reader in) {
		source = new Source(in);

		keywords = new HashMap<>();
		keywords.put("as", TokenType.AS);
		keywords.put("and", TokenType.AND);
		keywords.put("bool", TokenType.BOOL);
		keywords.put("do", TokenType.DO);
		keywords.put("else", TokenType.ELSE);
		keywords.put("false", TokenType.FALSE);
		keywords.put("float", TokenType.FLOAT);
		keywords.put("func", TokenType.FUNC);
		keywords.put("global", TokenType.GLOBAL);
		keywords.put("if", TokenType.IF);
		keywords.put("int", TokenType.INT);
		keywords.put("lambda", TokenType.LAMBDA);
		keywords.put("load", TokenType.LOAD);
		keywords.put("list", TokenType.LIST);
		keywords.put("mod", TokenType.MOD);
		keywords.put("none", TokenType.NONE);
		keywords.put("not", TokenType.NOT);
		keywords.put("or", TokenType.OR);
		keywords.put("print", TokenType.PRINT);
		keywords.put("prompt", TokenType.PROMPT);
		keywords.put("random", TokenType.RANDOM);
		keywords.put("str", TokenType.STR);
		keywords.put("times", TokenType.TIMES);
		keywords.put("true", TokenType.TRUE);
		keywords.put("typeof", TokenType.TYPEOF);
		keywords.put("while", TokenType.WHILE);

		opsAndPunct = new HashMap<>();
		opsAndPunct.put("&&", TokenType.AND);
		opsAndPunct.put("=", TokenType.ASSIGN);
		opsAndPunct.put(":", TokenType.COLON);
		opsAndPunct.put(",", TokenType.COMMA);
		opsAndPunct.put("/", TokenType.DIV);
		opsAndPunct.put("==", TokenType.EQ);
		opsAndPunct.put("**", TokenType.EXPONENT);
		opsAndPunct.put("->", TokenType.IF);
		opsAndPunct.put("//", TokenType.INTDIV);
		opsAndPunct.put(">=", TokenType.GE);
		opsAndPunct.put(">", TokenType.GT);
		opsAndPunct.put(">>", TokenType.LAMBDA);
		opsAndPunct.put("{", TokenType.LBRACE);
		opsAndPunct.put("[", TokenType.LBRACK);
		opsAndPunct.put("<=", TokenType.LE);
		opsAndPunct.put("(", TokenType.LPAR);
		opsAndPunct.put("<", TokenType.LT);
		opsAndPunct.put("-", TokenType.MINUS);
		opsAndPunct.put("%", TokenType.MOD);
		opsAndPunct.put("!=", TokenType.NE);
		opsAndPunct.put("<>", TokenType.NE);
		opsAndPunct.put("!", TokenType.NOT);
		opsAndPunct.put("||", TokenType.OR);
		opsAndPunct.put(".", TokenType.PERIOD);
		opsAndPunct.put("+", TokenType.PLUS);
		opsAndPunct.put("\"", TokenType.QUOTE);
		opsAndPunct.put("}", TokenType.RBRACE);
		opsAndPunct.put("]", TokenType.RBRACK);
		opsAndPunct.put(")", TokenType.RPAR);
		opsAndPunct.put(";", TokenType.SEMICOLON);
		opsAndPunct.put("*", TokenType.STAR);
	}

	public Token next() {
		int state = 0;
		StringBuilder lexeme = new StringBuilder();
		int startLine = source.line;
		int startColumn = source.column;

		while (true) {
			switch (state) {
				case 0:
					if (source.atEOF) {
						return new Token(source.line, source.column, TokenType.EOF, null);
					} else if (source.current == '.') {
						startLine = source.line;
						startColumn = source.column;
						lexeme.append(source.current);
						source.advance();
						state = -1;
					} else if (source.current == '0') {
						startLine = source.line;
						startColumn = source.column;
						lexeme.append(source.current);
						source.advance();
						state = 1;
					} else if (Character.isDigit(source.current)) {
						startLine = source.line;
						startColumn = source.column;
						lexeme.append(source.current);
						source.advance();
						state = 2;
					} else if (Character.isLetter(source.current) || source.current == '_') {
						startLine = source.line;
						startColumn = source.column;
						lexeme.append(source.current);
						source.advance();
						state = 3;
					} else if ("&=:,/=>{<(-%!|.+#\"});*[]".contains(String.valueOf(source.current))) {
						startLine = source.line;
						startColumn = source.column;
						lexeme.append(source.current);
						source.advance();
						state = 4;
						if (String.valueOf(lexeme).equals("=") || String.valueOf(lexeme).equals(">")
								|| String.valueOf(lexeme).equals("!")) {
							state = 9;
						} else if (String.valueOf(lexeme).equals("<")) {
							state = 10;
						} else if (String.valueOf(lexeme).equals("\"")) {
							state = 11;
						} else if (String.valueOf(lexeme).equals("/")) {
							state = 5;
						} else if (String.valueOf(lexeme).equals("#")) {
							state = 8;
						} else if (String.valueOf(lexeme).equals("&")) {
							state = 13;
						} else if (String.valueOf(lexeme).equals("|")) {
							state = 14;
						} else if (String.valueOf(lexeme).equals("-")) {
							state = 15;
						} else if (String.valueOf(lexeme).equals("*")) {
							state = 16;
						}
					} else if (Character.isWhitespace(source.current)) {
						source.advance();
					} else {
						// System.err.println("Illegal character: " + source.current);
						// System.err.println(" at " + source.line + ":" + source.column);
						// source.advance();
						throw new SyntaxError(
								"Illegal character: " + source.current + " at " + source.line + ":" + source.column
						);
						
					}
					break;
	
				case -1:
					if (source.atEOF || !Character.isDigit(source.current)) {
						return new Token(startLine, startColumn, TokenType.NUM, lexeme.toString());
					} else {
						lexeme.append(source.current);
						source.advance();
					}
					break;
	
				case 1:
					if (source.current == '.') {
						lexeme.append(source.current);
						source.advance();
						state = -1;
					} else {
						return new Token(startLine, startColumn, TokenType.NUM, lexeme.toString());
					}
	
				case 2:
					if (source.current == '.') {
						lexeme.append(source.current);
						source.advance();
						state = -1;
					} else if (source.atEOF || !Character.isDigit(source.current)) {
						return new Token(startLine, startColumn, TokenType.NUM, lexeme.toString());
					} else {
						lexeme.append(source.current);
						source.advance();
					}
					break;
	
				case 3:
					if (source.atEOF || !Character.isLetterOrDigit(source.current)) {
						if (source.current == '_' || source.current == '.') {
							lexeme.append(source.current);
							source.advance();
						} else {
							String lex = lexeme.toString();
							if (keywords.containsKey(lex)) {
								return new Token(startLine, startColumn, keywords.get(lex), null);
							} else {
								return new Token(startLine, startColumn, TokenType.ID, lex);
							}
						}
					} else {
						lexeme.append(source.current);
						source.advance();
					}
					break;
	
				case 4:
					return new Token(startLine, startColumn, opsAndPunct.get(lexeme.toString()), null);
	
				case 5:
					if (source.atEOF) {
						// System.err.println("Malformed comment: found " + source.current + " after /");
						// System.err.println(" at " + source.line + ":" + source.column);
						// state = 0;
						throw new SyntaxError(
								"Malformed comment: found " + source.current + " after /\n"
								+ " at " + source.line + ":" + source.column
							);
					} else if (source.current == '/') {
						lexeme.append(source.current);
						source.advance();
						state = 4;
					} else if (Character.isDigit(source.current) || Character.isWhitespace(source.current)) {
						state = 4;
					} else if (source.current != '*') {
						// System.err.println("Malformed comment: found " + source.current + " after /");
						// System.err.println(" at " + source.line + ":" + source.column);
						// state = 0;
						throw new SyntaxError(
								"Malformed comment: found " + source.current + " after /\n"
								+ " at " + source.line + ":" + source.column
							);
					} else if (source.current == '*') {
						source.advance();
						state = 6;
					}
					break;
	
				case 6:
					if (source.atEOF) {
						// System.err.println("Unclosed comment at end of file");
						// state = 0;
						throw new SyntaxError("Unclosed comment at end of file");
					} else if (source.current == '*') {
						source.advance();
						state = 7;
					} else {
						source.advance();
					}
					break;
	
				case 7:
					if (source.atEOF) {
						// System.err.println("Unclosed comment at end of file");
						// state = 0;
						throw new SyntaxError("Unclosed comment at end of file");
					} else if (source.current == '/') {
						source.advance();
						lexeme.delete(0, lexeme.length());
						state = 0;
					} else if (source.current == '*') {
						source.advance();
					} else {
						source.advance();
						state = 6;
					}
					break;
	
				case 8:
					if (source.atEOF || source.current == '\n') {
						lexeme.delete(0, lexeme.length());
						state = 0;
					} else {
						source.advance();
					}
					break;
	
				case 9:
					if (source.current == '=') {
						lexeme.append(source.current);
						source.advance();
					} else if (lexeme.toString().equals(">") && source.current == '>') {
						lexeme.append(source.current);
						source.advance();
					}
					state = 4;
					break;
	
				case 10:
					if (source.current == '=' || source.current == '>') {
						lexeme.append(source.current);
						source.advance();
					}
					state = 4;
					break;
	
				case 11:
					if (source.atEOF) {
						// System.err.println("Unclosed String at end of file");
						// state = 0;
						throw new SyntaxError("Unclosed String at end of file");
					} else if (source.current == '\n') {
						// System.err.println("Unclosed String at end of line");
						// lexeme.delete(0, lexeme.length());
						// state = 0;
						throw new SyntaxError("Unclosed String at end of line");
					} else if (source.current == '"') {
						lexeme.append(source.current);
						source.advance();
						state = 12;
					} else if (source.current == '\\') {
						lexeme.append("\n");
						source.advance();
					} else {
						lexeme.append(source.current);
						source.advance();
					}
					break;
	
				case 12:
					if (source.current == '"') {
						source.advance();
						state = 11;
					} else if (source.current == '\n') {
						source.advance();
						return new Token(startLine, startColumn, TokenType.STRING, lexeme.toString());
					} else {
						return new Token(startLine, startColumn, TokenType.STRING, lexeme.toString());
					}
					break;
	
				case 13:
					if (source.current == '&') {
						lexeme.append(source.current);
						source.advance();
					}
					state = 4;
					break;
	
				case 14:
					if (source.current == '|') {
						lexeme.append(source.current);
						source.advance();
					}
					state = 4;
					break;
	
				case 15:
					if (source.current == '>') {
						lexeme.append(source.current);
						source.advance();
					}
					state = 4;
					break;
				
				case 16:
					if (source.current == '*') {
						lexeme.append(source.current);
						source.advance();
					}
					state = 4;
					break;
			}
		}
	}

	public void close() throws IOException {
		source.close();
	}

	private Source source;
	private Map<String, TokenType> opsAndPunct;
	static Map<String, TokenType> keywords;

}
