package callpkg;

public class ObjectName{
	private String name;

	public ObjectName(String name) {
		this.name = name;
	}

	public String Name() {
		return this.name;
	}

	public String Name(String newname) {
		String oldname = this.name;
		this.name = newname;
		return oldname;
	}
}