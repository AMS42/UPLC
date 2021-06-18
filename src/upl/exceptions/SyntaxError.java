package upl.exceptions;

@SuppressWarnings("serial")
public class SyntaxError extends UPLError {

	public SyntaxError(String message) {
		super("SyntaxError: " + message);
	}

	public SyntaxError(String message, Throwable cause) {
		super("SyntaxError: " + message, cause);
	}
}