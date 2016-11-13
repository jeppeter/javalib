package namespace;

public class NameSpaceEx {
	public NameSpaceEx() {
	}

	public static NameSpaceEx name_handler(NameSpaceEx args,Object ctx) {
		System.out.println("name handler");
		return args;
	}
}