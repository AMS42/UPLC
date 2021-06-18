package upl.compiler;

class Param {

	public Param(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void display(String indent) {
		System.out.println(indent + id);
	}

	private String id;
}
