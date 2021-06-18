package upl.compiler;

import java.io.IOException;
import java.io.Reader;

class Source {

	public Source(Reader in) {
		this.in = in;
		this.line = 0;
		this.column = 0;
		this.current = '\n';
		this.atEOF = false;

		advance();
	}

	public void advance() {
		if (atEOF)
			return;

		if (current == '\n') {
			++line;
			column = 1;
		} else {
			++column;
		}

		try {
			int next = in.read();
			if (next == -1) {
				atEOF = true;
			} else {
				current = (char) next;
			}
		} catch (IOException e) {
			System.err.println("Error: " + e);
			System.exit(1);
		}
	}

	public void close() throws IOException {
		in.close();
	}

	private Reader in;

	int line, column;
	char current;
	boolean atEOF;

}
