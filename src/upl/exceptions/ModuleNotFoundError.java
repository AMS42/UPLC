package upl.exceptions;

@SuppressWarnings("serial")
public class ModuleNotFoundError extends UPLError{
	public ModuleNotFoundError(String message) {
		super("ModuleNotFoundError: " + message);
	}

	public ModuleNotFoundError(String message, Throwable cause) {
		super("ModuleNotFoundError: " + message, cause);
	}
}
