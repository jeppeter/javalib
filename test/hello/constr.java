

public class constr {
	public static void main(String... args) {
		String sstr;
		String basestr;
		int i;
		if (args.length < 2) {
			System.err.printf("constr searchstr basestr...\n");
			System.exit(-1);
		}

		sstr = args[0];
		for (i=1;i<args.length;i++) {
			basestr = args[i];
			if (basestr.contains(sstr)) {
				System.out.printf("(%s) contains (%s)\n",basestr,sstr);
			} else {
				System.out.printf("(%s) not contains (%s)\n",basestr,sstr);
			}
		}

		return;
	}
}