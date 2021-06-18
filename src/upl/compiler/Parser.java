package upl.compiler;

import java.util.ArrayList;
import java.util.List;
import upl.exceptions.SyntaxError;
import upl.exceptions.TypeError;

class Parser {

	public Parser(Scanner scanner) {
		this.scanner = scanner;
		this.current = scanner.next();
	}

	public Program parseProgram() {
		if (check(TokenType.EOF)) {
			return new Program(new Block(new ArrayList<Stmt>()));
		}
		Block block = parseBlock();
		match(TokenType.EOF);
		return new Program(block);
	}

	public Block parseBlock() {
		List<Stmt> stmts = parseStmtList();
		return new Block(stmts);
	}

	public List<Stmt> parseStmtList() {
		return parseStmts();
	}

	public List<Stmt> parseStmts() {
		Stmt stmt = parseStmt();
		if (check(TokenType.SEMICOLON)) {
			match(TokenType.SEMICOLON);
			if (check(TokenType.EOF, TokenType.RBRACE)) {
				List<Stmt> s = new ArrayList<>();
				s.add(0, stmt);
				return s;
			}
		} else {
			// System.err.println("Expected SEMICOLON but found " + current);
			// System.exit(1);
			// return null;
			throw new SyntaxError("Expected SEMICOLON but found " + current);
		} 
		return parseStmtRest(stmt);
	}

	public List<Stmt> parseStmtRest(Stmt s) {
		List<Stmt> ss = parseStmts();
		ss.add(0, s);
		return ss;
	}

	public Stmt parseStmt() {
		if (check(TokenType.DO)) {
			return parseDo();
		} else if (check(TokenType.FUNC)) {
			return parseFunDecl();
		} if (check(TokenType.GLOBAL)) {
			match(TokenType.GLOBAL);
			List<String> globals = new ArrayList<>();
			globals.add(match(TokenType.ID).lexeme);
			while (check(TokenType.COMMA)) {
				match(TokenType.COMMA);
				globals.add(match(TokenType.ID).lexeme);
			}
			return new Global(globals);
		} else if (check(TokenType.ID)) {
			return parseId();
		} else if (check(TokenType.IF)) {
			return parseIf();
		} else if (check(TokenType.LOAD)) {
			match(TokenType.LOAD);
			String file = match(TokenType.ID).lexeme;
			if (check(TokenType.AS)) {
				match(TokenType.AS);
				String id = match(TokenType.ID).lexeme;
				return new Load(file, id);
			}
			return new Load(file);
		} else if (check(TokenType.PRINT)) {
			match(TokenType.PRINT);
			match(TokenType.LPAR);
			List<Item> items = parseItems();
			match(TokenType.RPAR);
			return new Print(items);
		} else if (check(TokenType.SEMICOLON)) {
			match(TokenType.SEMICOLON);
			return parseStmt();
		} else if (check(TokenType.WHILE)) {
			return parseWhile();
		} else if (check(TokenType.FALSE, TokenType.LBRACE, TokenType.LBRACK, TokenType.LPAR, TokenType.MINUS,
				TokenType.NONE, TokenType.NOT, TokenType.NUM, TokenType.PROMPT, TokenType.STRING, TokenType.TRUE,
				TokenType.TYPEOF, TokenType.BOOL, TokenType.FLOAT, TokenType.INT, TokenType.LIST, TokenType.STR,
				TokenType.RANDOM)) {
			Expr expr = parseExpr();
			return new ExprStmt(expr);
		} else {
			// System.err.println("Error: Invalid Expression Statement " + current);
			// System.exit(1);
			// return null;
			throw new SyntaxError("Invalid Expression Statement " + current);
		}
	}

	public Stmt parseDo() {
		match(TokenType.DO);
		Expr num = parseExpr();
		match(TokenType.TIMES);
		match(TokenType.LBRACE);
		Block body = parseBlock();
		match(TokenType.RBRACE);
		return new For(num, body);
	}

	public Stmt parseFunDecl() {
		match(TokenType.FUNC);
		String id = match(TokenType.ID).lexeme;
		match(TokenType.LPAR);
		List<Param> list = parseParam();
		match(TokenType.RPAR);
		match(TokenType.LBRACE);
		Block block = parseBlock();
		match(TokenType.RBRACE);
		return new FuncDecl(id, list, block);
	}

	public List<Param> parseParam() {
		ArrayList<Param> p = new ArrayList<Param>();
		while (check(TokenType.ID)) {
			p.add(parseParams());
		}
		return p;
	}

	public Param parseParams() {
		String name = match(TokenType.ID).lexeme;
		if (check(TokenType.RPAR, TokenType.COLON)) {
			return new Param(name);
		} else {
			match(TokenType.COMMA);
		}
		return new Param(name);
	}

	public Stmt parseId() {
		String id = match(TokenType.ID).lexeme;
		if (check(TokenType.ASSIGN)) {
			match(TokenType.ASSIGN);
			Expr expr = parseExpr();
			return new Assign(id, expr);
		} else if (check(TokenType.LPAR)) {
			Expr expr = parseFactorRest(id);
			return new ExprStmt(expr);
		} else if (check(TokenType.SEMICOLON)) {
			return new ExprStmt(new Id(id));
		} else if (check(TokenType.LBRACK)) {
			match(TokenType.LBRACK);
			Expr expr = parseExpr();
			match(TokenType.RBRACK);
			if (check(TokenType.ASSIGN)) {
				match(TokenType.ASSIGN);
				Expr value = parseExpr();
				return new IndexAssign(new Id(id), expr, value);
			} else if (check(TokenType.EQ, TokenType.GE, TokenType.GT, TokenType.LE, TokenType.LT, TokenType.NE)) {
				Op2 op;
				Expr right;
				op = parseRelOp();
				right = parseExpr();
				return new ExprStmt(new BinOp(new Index(new Id(id), expr), op, right));
			} else if (check(TokenType.MINUS, TokenType.OR, TokenType.PLUS)) {
				Op2 op;
				Expr right;
				op = parseAddOp();
				right = parseExpr();
				return new ExprStmt(new BinOp(new Index(new Id(id), expr), op, right));
			} else if (check(TokenType.AND, TokenType.DIV, TokenType.EXPONENT, TokenType.INTDIV, TokenType.MOD, TokenType.STAR)) {
				Op2 op;
				Expr right;
				op = parseMultOp();
				right = parseExpr();
				return new ExprStmt(new BinOp(new Index(new Id(id), expr), op, right));
			}
			return new ExprStmt(new Index(new Id(id), expr));
		} else {
			Op2 op;
			Expr right;
			if (check(TokenType.EQ, TokenType.GE, TokenType.GT, TokenType.LE, TokenType.LT, TokenType.NE)) {
				op = parseRelOp();
			} else if (check(TokenType.MINUS, TokenType.OR, TokenType.PLUS)) {
				op = parseAddOp();
			} else if (check(TokenType.AND, TokenType.DIV, TokenType.EXPONENT, TokenType.INTDIV, TokenType.MOD, TokenType.STAR)) {
				op = parseMultOp();
			} else {
				return new ExprStmt(new Id(id));
			}
			right = parseExpr();
			return new ExprStmt(new BinOp(new Id(id), op, right));
		}
	}

	public Stmt parseIf() {
		match(TokenType.IF);
		Expr expr = parseExpr();
		match(TokenType.LBRACE);
		Block trueClause = parseBlock();
		match(TokenType.RBRACE);
		if (check(TokenType.ELSE)) {
			match(TokenType.ELSE);
			if (check(TokenType.IF)) {
				ArrayList<Stmt> falseClause = new ArrayList<>();
				falseClause.add(parseIf());
				return new IfElse(expr, trueClause, new Block(falseClause));
			} else {
				match(TokenType.LBRACE);
				Block falseClause = parseBlock();
				match(TokenType.RBRACE);
				return new IfElse(expr, trueClause, falseClause);
			}
		} else {
			return new If(expr, trueClause);
		}
	}

	public Stmt parseWhile() {
		match(TokenType.WHILE);
		Expr expr = parseExpr();
		match(TokenType.LBRACE);
		Block body = parseBlock();
		match(TokenType.RBRACE);
		return new While(expr, body);
	}

	public Expr parseTypeCast() {
		TokenType type = current.type;
		if (check(TokenType.BOOL)) {
			match(TokenType.BOOL);
		} else if (check(TokenType.FLOAT)) {
			match(TokenType.FLOAT);
		} else if (check(TokenType.INT)) {
			match(TokenType.INT);
		} else if (check(TokenType.STR)) {
			match(TokenType.STR);
		} else if (check(TokenType.LIST)) {
			match(TokenType.LIST);
		} else {
			// System.err.println("Invalid Type for Casting " + current);
			throw new TypeError("Invalid Type for Casting " + current);
		}
		match(TokenType.LPAR);
		Expr expr = parseExpr();
		match(TokenType.RPAR);
		return new TypeCast(type, expr);
	}

	public List<Item> parseItems() {
		Item it = parseItem();
		return parseItemRest(it);
	}

	public List<Item> parseItemRest(Item it) {
		if (check(TokenType.COMMA)) {
			match(TokenType.COMMA);
			List<Item> its = parseItems();
			its.add(0, it);
			return its;
		} else {
			List<Item> its = new ArrayList<Item>();
			its.add(it);
			return its;
		}
	}

	public Item parseItem() {
		if (check(TokenType.BOOL, TokenType.FALSE, TokenType.FLOAT, TokenType.ID, TokenType.INT, TokenType.LBRACK,
				TokenType.LIST, TokenType.LPAR, TokenType.MINUS, TokenType.NONE, TokenType.NOT, TokenType.NUM,
				TokenType.PROMPT, TokenType.RANDOM, TokenType.STR, TokenType.STRING, TokenType.TRUE, TokenType.TYPEOF)) {
			Expr expr = parseExpr();
			return new ExprItem(expr);
		} else if (check(TokenType.RPAR)) {
			return new StringItem("");
		} else {
			// System.err.println("Invalid item: " + current);
			// System.exit(1);
			// return null;
			throw new SyntaxError("Invalid item: " + current);
		}
	}

	public Expr parseExpr() {
		Expr left = parseSimpleExpr();
		return parseExprRest(left);
	}

	public Expr parseExprRest(Expr left) {
		if (check(TokenType.EQ, TokenType.GE, TokenType.GT, TokenType.LE, TokenType.LT, TokenType.NE)) {
			Op2 op = parseRelOp();
			Expr right = parseSimpleExpr();
			return new BinOp(left, op, right);
		} else {
			return left;
		}
	}

	public Expr parseSimpleExpr() {
		Expr leftT = parseTerm();
		while (check(TokenType.MINUS, TokenType.OR, TokenType.PLUS)) {
			Op2 op = parseAddOp();
			Expr rightT = parseTerm();
			leftT = new BinOp(leftT, op, rightT);
		}
		return leftT;
	}

	public Op2 parseRelOp() {
		if (check(TokenType.EQ)) {
			match(TokenType.EQ);
			return Op2.EQ;
		} else if (check(TokenType.GE)) {
			match(TokenType.GE);
			return Op2.GE;
		} else if (check(TokenType.GT)) {
			match(TokenType.GT);
			return Op2.GT;
		} else if (check(TokenType.LE)) {
			match(TokenType.LE);
			return Op2.LE;
		} else if (check(TokenType.LT)) {
			match(TokenType.LT);
			return Op2.LT;
		} else if (check(TokenType.NE)) {
			match(TokenType.NE);
			return Op2.NE;
		} else {
			// System.err.println("Error: Invalid Operation Statement " + current);
			// System.exit(1);
			// return null;
			throw new SyntaxError("Invalid Relational Operation Statement " + current);
		}
	}

	public Op2 parseAddOp() {
		if (check(TokenType.MINUS)) {
			match(TokenType.MINUS);
			return Op2.MINUS;
		} else if (check(TokenType.OR)) {
			match(TokenType.OR);
			return Op2.OR;
		} else if (check(TokenType.PLUS)) {
			match(TokenType.PLUS);
			return Op2.PLUS;
		} else {
			// System.err.println("Invalid Addition Operator: " + current);
			// System.exit(1);
			// return null;
			throw new SyntaxError("Invalid Addition Operator: " + current);
		}
	}

	public Op2 parseMultOp() {
		if (check(TokenType.AND)) {
			match(TokenType.AND);
			return Op2.AND;
		} else if (check(TokenType.DIV)) {
			match(TokenType.DIV);
			return Op2.DIV;
		} else if (check(TokenType.EXPONENT)) {
			match(TokenType.EXPONENT);
			return Op2.EXP;
		} else if (check(TokenType.INTDIV)) {
			match(TokenType.INTDIV);
			return Op2.INTDIV;
		} else if (check(TokenType.MOD)) {
			match(TokenType.MOD);
			return Op2.MOD;
		} else if (check(TokenType.STAR)) {
			match(TokenType.STAR);
			return Op2.TIMES;
		} else {
			// System.err.println("Invalid Multiplication Operator: " + current);
			// System.exit(1);
			// return null;
			throw new SyntaxError("Invalid Multiplication Operator: " + current);
		}
	}

	public Op1 parseUnOp() {
		if (check(TokenType.MINUS)) {
			match(TokenType.MINUS);
			return Op1.NEG;
		} else if (check(TokenType.NOT)) {
			match(TokenType.NOT);
			return Op1.NOT;
		} else {
			// System.err.println("Invalid Unary Operator: " + current);
			// System.exit(1);
			// return null;
			throw new SyntaxError("Invalid Unary Operator: " + current);
		}
	}

	public Expr parseTerm() {
		Expr leftF = parseFactor();
		while (check(TokenType.AND, TokenType.DIV, TokenType.EXPONENT, TokenType.INTDIV, TokenType.MOD, TokenType.STAR)) {
			Op2 op = parseMultOp();
			Expr rightF = parseFactor();
			leftF = new BinOp(leftF, op, rightF);
		}
		return leftF;
	}

	public Expr parseFactor() {
		if (check(TokenType.FALSE)) {
			match(TokenType.FALSE);
			return new False();
		} else if (check(TokenType.ID)) {
			String id = match(TokenType.ID).lexeme;
			return parseFactorRest(id);
		} else if (check(TokenType.LAMBDA)) {
			match(TokenType.LAMBDA);
			List<Param> ps = parseParam();
			match(TokenType.COLON);
			Stmt s = parseStmt();
			return new LambdaDecl(ps, s);
		} else if (check(TokenType.LPAR)) {
			match(TokenType.LPAR);
			Expr expr = parseExpr();
			match(TokenType.RPAR);
			return expr;
		} else if (check(TokenType.LBRACK)) {
			match(TokenType.LBRACK);
			if (check(TokenType.RBRACK)) {
				match(TokenType.RBRACK);
				return new upl.compiler.List(new Object[0]);
			}
			Object[] listValues = parseItems().toArray();
			match(TokenType.RBRACK);
			if (check(TokenType.LBRACK)) {
				match(TokenType.LBRACK);
				Expr index = parseExpr();
				match(TokenType.RBRACK);
				return new Index(new upl.compiler.List(listValues), index);
			}
			return new upl.compiler.List(listValues);
		} else if (check(TokenType.MINUS, TokenType.NOT)) {
			Op1 op = parseUnOp();
			Expr expr = parseFactor();
			return new UnOp(op, expr);
		} else if (check(TokenType.NONE)) {
			match(TokenType.NONE);
			return new None();
		} else if (check(TokenType.NUM)) {
			String value = match(TokenType.NUM).lexeme;
			return new Num((value.contains(".") ? Float.parseFloat(value) : Integer.parseInt(value)),
					!value.contains("."));
		} else if (check(TokenType.PROMPT)) {
			match(TokenType.PROMPT);
			match(TokenType.LPAR);
			String msg = match(TokenType.STRING).lexeme;
			match(TokenType.RPAR);
			return new Prompt(msg);
		} else if (check(TokenType.RANDOM)) {
			match(TokenType.RANDOM);
			match(TokenType.LPAR);
			Expr lowerExpr = null;
			Expr upperExpr = parseExpr();
			if (check(TokenType.COMMA)) {
				match(TokenType.COMMA);
				lowerExpr = upperExpr;
				upperExpr = parseExpr();
			}
			match(TokenType.RPAR);
			return new Random(lowerExpr, upperExpr);
		} else if (check(TokenType.STRING)) {
			String str = match(TokenType.STRING).lexeme;
			return new Str(str);
		} else if (check(TokenType.TRUE)) {
			match(TokenType.TRUE);
			return new True();
		} else if (check(TokenType.TYPEOF)) {
			match(TokenType.TYPEOF);
			match(TokenType.LPAR);
			Expr expr = parseExpr();
			match(TokenType.RPAR);
			return new TypeOf(expr);
		} else if (check(TokenType.BOOL, TokenType.FLOAT, TokenType.INT, TokenType.LIST, TokenType.STR)) {
			return parseTypeCast();
		} else {
			// System.err.println("Invalid Factor: " + current);
			// System.exit(1);
			// return null;
			throw new SyntaxError("Invalid Factor: " + current);
		}
	}

	public Expr parseFactorRest(String id) {
		if (check(TokenType.LPAR)) {
			match(TokenType.LPAR);
			List<Expr> args = parseArgList();
			match(TokenType.RPAR);
			return new Call(id, args);
		} else if (check(TokenType.LBRACK)) {
			match(TokenType.LBRACK);
			Expr index = parseExpr();
			match(TokenType.RBRACK);
			return new Index(new Id(id), index);
		} else {
			return new Id(id);
		}
	}

	public List<Expr> parseArgList() {
		if (check(TokenType.BOOL, TokenType.FALSE, TokenType.FLOAT, TokenType.ID, TokenType.INT, TokenType.LIST,
				TokenType.LBRACK, TokenType.LPAR, TokenType.MINUS, TokenType.NONE, TokenType.NOT, TokenType.NUM,
				TokenType.STR, TokenType.STRING, TokenType.TRUE)) {
			return parseArgs();
		} else {
			return new ArrayList<Expr>();
		}
	}

	public List<Expr> parseArgs() {
		Expr a = parseExpr();
		return parseArgsRest(a);
	}

	public List<Expr> parseArgsRest(Expr a) {
		if (check(TokenType.COMMA)) {
			match(TokenType.COMMA);
			List<Expr> as = parseArgs();
			as.add(0, a);
			return as;
		} else {
			List<Expr> args = new ArrayList<Expr>();
			args.add(a);
			return args;
		}
	}

	private Token match(TokenType type) {
		if (current.type == type) {
			Token result = current;
			current = scanner.next();
			return result;
		} else {
			// System.err.println("Expected " + type + " but found " + current);
			// System.exit(1);
			// return null;
			throw new SyntaxError("Expected " + type + " but found " + current);
		}
	}

	private boolean check(TokenType... types) {
		for (TokenType type : types) {
			if (current.type == type) {
				return true;
			}
		}
		return false;
	}

	public Token getToken() {
		return current;
	}

	private Scanner scanner;
	private Token current;
}
