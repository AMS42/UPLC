package upl.exceptions;

@SuppressWarnings("serial")
public class TypeError extends UPLError {

	public TypeError(String message) {
		super("TypeError: " + message);
	}

	public TypeError(String message, Throwable cause) {
		super("TypeError: " + message, cause);
	}
}
