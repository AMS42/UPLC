package upl.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import upl.exceptions.ModuleNotFoundError;

class Load extends Stmt {
	
	public Load(String fileName) {
		this.fileName = fileName.replace(".", "/");
		this.refName = fileName;
	}
	
	public Load(String fileName, String refName) {
		this.fileName = fileName.replace(".", "/");
		this.refName = refName;
	}

	@Override
	public Value interpret(SymbolTable t) throws Error {
		try {
			File f = new File(fileName + ".upl");
			String abs = f.getAbsolutePath();
			Parser p = new Parser(
				new Scanner(
					new FileReader(
						new File(abs)
					)
				)
			);
			Block b = p.parseBlock();
			t.enter(refName);
			Value ret = b.interpret(t);
			t.exit(refName);
			return ret;
		} catch (FileNotFoundException e) {
			throw new ModuleNotFoundError("Could not find UPL module: " + fileName);
		}
	}

	@Override
	public void display(String indent) {
		System.out.println(indent + "Load " + fileName);	
	}

	private String fileName, refName;
}
