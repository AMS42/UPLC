package upl.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;

public class Driver {

	public static void main(String[] args) {
		argv = args;
		if (args.length <= 0) {
			System.out.println("UPL (v" + VERSION + ")");
			String cmd;
			SymbolTable t = new SymbolTable();
			ArrayList<String> cmds = new ArrayList<>();
			do {
				@SuppressWarnings("unused")
				int i = cmds.size();
				System.out.print(">>> ");
				@SuppressWarnings("resource")
				java.util.Scanner s = new java.util.Scanner(System.in);
				cmd = s.nextLine();
				if (cmd.startsWith("^[[A")) {
					boolean invalidUps = false;
					String[] ups = cmd.split("^[[A");
					for (String up : ups) {
						if (!up.equals("^[[A")) {
							invalidUps = true;
							break;
						}
					}
					if (invalidUps) {
						System.err.println("Invalid Arrow Sequence");
						continue;
					}
					int num_ups = ups.length;
					cmd = cmds.get(i - num_ups);
					System.out.println("> Execute " + cmd + " (Y/N)");
					String execute = s.nextLine().toLowerCase();
					if (!execute.equals("yes") && !execute.equals("y")) {
						continue;
					}
				}
				cmds.add(cmd);
				if (cmd.equals("exit()")) { break; }  // exits REPL
				else if (cmd.isBlank()) { continue; }  // doesn't execute empty program (blank)
				else if (cmd.strip().equals(";")) { continue; }  // doesn't execute empty program (only semicolon)
				else if (cmd.startsWith("/*") && cmd.endsWith("*/")) { continue; }  // doesn't execute empty program (only comment)
				else if (cmd.contains("#")) { cmd = cmd.split("#")[0]; if (cmd.isBlank()) continue; }  // separates single line comments from statement
				else if (cmd.startsWith("\"") || cmd.endsWith("\"")) { cmd += ";"; } // can only parse string literals at EOF with semicolon
				try {
					Parser parser = new Parser(new Scanner(new StringReader(cmd)));
					Stmt stmt = parser.parseStmt();
					Value msg = stmt.interpret(t);
					if (msg.stringValue().equals("none")) { continue; }  // don't print none to REPL
					else if (cmd.contains("=") 
							&& !cmd.contains("==")
							&& !cmd.contains("!=")
							&& !cmd.contains("<=")
							&& !cmd.contains(">=")
						) { continue; } // don't print assignment values to REPL 
					String type;
					if (msg instanceof BoolValue) {type = "bool"; } 
					else if (msg instanceof FloatValue) { type = "float"; } 
					else if (msg instanceof IntValue) {  type = "int"; }
					else if (msg instanceof StringValue) { type = "str"; }
					else if (msg instanceof ListValue) { type = "list"; }
					else if (msg instanceof FuncValue || msg instanceof LambdaValue) { type = "func"; }
					else { type = "none"; }
					
					System.out.println(
							(
								type.equals("str") ?
									"'" + msg.stringValue() + "'" :
									msg.stringValue()
							) + " : " + type
						);
				} catch (Exception e) { 
					continue; 
				} catch (Error e) {
					System.err.print(e.getMessage());
					System.out.println();
				}
			} while (true);
		} else if (args.length > 1) {
			if (args[0].equals("-c")) {
				StringBuilder sp = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					sp.append(args[i]);
					if (!args[i].endsWith(";")) {
						sp.append(";");
					}
				}
				try {
					Parser parser = new Parser(new Scanner(new StringReader(sp.toString())));
					Program program = parser.parseProgram();
					program.interpret();
				} catch (Error e) {
					System.err.println(e);
				}
			} else if (args[0].equals("-o")) {
				try {
					System.setOut(new PrintStream(args[2]));
					Parser parser = new Parser(new Scanner(new FileReader(new File(args[1]))));
					Program program = parser.parseProgram();
					program.interpret();
				} catch (FileNotFoundException e) {
					System.err.println("File not found");
					System.err.println("File: " + e.getMessage());
					System.exit(1);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println("-o <infile> <outfile>");
					System.exit(1);
				} catch (Error e) {
					System.err.println(e);
					System.exit(1);
				}
			} else if (args[0].contentEquals("-h")) {
				System.out.println("Usage: uplc [args...]\n");
				System.out.println("[args...] include the following:");
					System.out.print("<noargs>");
						System.out.println("\t: runs UPL repl");
					System.out.print("<file>");
						System.out.println("\t: compiles and executes <file> passed as a file path");
					System.out.print("-c <program>");
						System.out.println("\t: compiles and executes <program> passed as string");
					System.out.print("-h");
						System.out.println("\t: displays this help message");
					System.out.print("-o <infile> <outfile>");
						System.out.println("\t: compiles and executes <infile> passed as");
						System.out.println("\t\t file path and sends output to <outfile> passed as file path");
			} else {
				System.err.println("Invalid run command");
				System.err.println("Try running command with -h");
				System.exit(1);
			}
		} else {
			try {
				Parser parser = new Parser(new Scanner(new FileReader(new File(args[0]))));
				Program program = parser.parseProgram();
				program.interpret();
			} catch (FileNotFoundException e) {
				System.err.println("File not found");
				System.err.println("File: " + args[0]);
				System.exit(1);
			} catch (Error e) {
				System.err.println(e);
				System.exit(1);
			}
		}
	}

	public static String[] argv;
	private static final String VERSION = "0.9.11";
	/* 
	 * Versions to Date
	 * 0.9.0 - It works
	 * 0.9.1 - Lists, Type Casting, and Empty Print Statements
	 * 0.9.2 - Mutable Lists through Indexing
	 * 0.9.3 - Operators for Indexes
	 * 0.9.4 - Loading Functions from Modules
	 * 0.9.5 - Exceptions
	 * 0.9.6 - Reformed Lambda Declarations
	 * 				Reformed Type Casts Values
	 * 0.9.7 - Reformed Referencing of Loaded Functions/Variables
	 * 				Introduction of "as"
	 * 0.9.8 - Fixing str type cast to float && int
	 * 0.9.9 - Subtraction and multiplication for lists and strings 
	 * 				Introduction of Exponents
	 * 				Reformed division and integer division
	 * 0.9.10 - Random with ints and floats
	 * 0.9.11 - Global side-effects with keyword (global)
	 * Future Build: 0.9.12 - Reformed use of semicolons
	 * Future Build: 0.9.13 - Use of arrows in REPL  - may have to hold off
	 * Future Build: 0.9.14 - Multi-Line Blocks in REPL
	 * Future Build: 0.9.15 - Help Function
	 * Future Build: 0.9.16 - Reconsider the grouping for parsing expressions
	 */

}
