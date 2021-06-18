package upl.compiler;

class StringItem extends Item {

	public StringItem(String message) {
		this.message = message;
	}

	public String getMsg() {
		return message;
	}

	public void display(String indent) {
		System.out.println(indent + "StringItem \"" + message + "\"");
	}

	private String message;
}
