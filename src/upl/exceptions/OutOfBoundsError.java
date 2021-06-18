package upl.exceptions;

@SuppressWarnings("serial")
public class OutOfBoundsError extends UPLError {

	public OutOfBoundsError(String message) {
		super("OutOfBoundsError: " + message);
	}

	public OutOfBoundsError(String message, Throwable cause) {
		super("OutOfBoundsError: " + message, cause);
	}

}
