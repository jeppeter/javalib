

class Thing {

	public static int number_of_things = 0;
	private String what;

	public Thing (String what) {
		this.what = what;
		number_of_things++;
		System.out.printf("constructor %d\n",number_of_things);
	}

	public String Get() {
		return this.what;
	}

	protected void finalize ()  {
		number_of_things--;
		System.out.printf("destructor %d\n",number_of_things);
	}

}
public class tstpkg {
	public static void main(String args[]) {
		Thing acir = new Thing("hello");
		System.out.printf("acir %s\n",acir.Get());
		acir.finalize();
		acir = null;
		return;
	}
}