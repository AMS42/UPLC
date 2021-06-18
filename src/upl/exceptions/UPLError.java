package upl.exceptions;

@SuppressWarnings("serial")
public class UPLError extends Error {
	public UPLError(String message) {
		super(message, null, false, true);
	}

	public UPLError(String message, Throwable cause) {
		super(message, cause, false, true);
	}

}
