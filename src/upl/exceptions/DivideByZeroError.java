package upl.exceptions;

@SuppressWarnings("serial")
public class DivideByZeroError extends UPLError {

	public DivideByZeroError(String message) {
		super("DivideByZeroError: " + message);
	}

	public DivideByZeroError(String message, Throwable cause) {
		super("DivideByZeroError: " + message, cause);
	}

}
