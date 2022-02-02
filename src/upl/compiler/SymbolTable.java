package upl.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Scanner;

class SymbolTable {
	public SymbolTable() {
		table = new Stack<>();
		modules = new ArrayList<>();
		enter();
		enter();
		bind("pi", new FloatValue((float) 3.14159265));
		bind("e", new FloatValue((float) 2.71828185));
	}

	public void bind(String id, Value value) {
		HashMap<String, Value> t = table.peek();
		if (loadBinds) {
			t.put(curPrefix + "." + id, value);
		} else if (table.get(1).containsKey(id)) {
			for (int i = table.size() - 1; i >= 0; i--) {
				if (table.get(table.size() - 1).containsKey("GLOBAL." + id)) {
					table.get(1).put(id, value);
					return;
				}
			}
			t.put(id, value);
		} else {
			t.put(id, value);
		}
	}
	
	public void globalBind(String id) {
		bind("GLOBAL." + id, table.get(1).getOrDefault(id, new NoneValue()));
	}

	public Value lookup(String id) {
		if (table.get(1).containsKey(id)) { 
			for (int i = table.size() - 1; i >= 0; i--) {
				if (table.get(table.size() - 1).containsKey("GLOBAL." + id)) {
					return table.get(1).get(id);
				}
			}
		}
		for (int i = table.size() - 1; i >= 0; i--) {
			if (table.get(i).containsKey(id)) {
				return table.get(i).get(id);
			}
		}
		for (int i = 0; i < modules.size(); i++) {
			if (table.get(i).containsKey(modules.get(i) + "." + id)) {
				return table.get(0).get(modules.get(i) + "." + id);
			}
		}

		Value defaultValue = new NoneValue();
		bind(id, defaultValue);
		return defaultValue;
	}

	public void enter() {
		table.push(new HashMap<>());
	}
	
	void enter(String prefix) {
		loadBinds = true;
		curPrefix = prefix;
		modules.add(curPrefix);
	}

	public void exit() {
		table.pop();
	}
	
	void exit(String prefix) {
		loadBinds = false;
		curPrefix = "";
	}

	private Stack<HashMap<String, Value>> table;
	private boolean loadBinds = false;
	private String curPrefix;
	private List<String> modules;
	public static Scanner sc = new Scanner(System.in);
	
	/* Notes:
	 * table.get(0) => meta-data (e.g. __name__, __file__, et al.)
	 * table.get(1) => global variables
	 * table.get(n) => functions and other subroutines and loops
	 */
}
