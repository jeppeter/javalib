import java.util.HashMap;


class Counter {
	private String m_name;
	public Counter() {
		this.m_name = "";
	}

	public void finalize() {
		this.m_name = "";
	}
}

class HashMapObj {
	
}

public class hashtest {
	public static void main(String... args) {
		HashMap<String,Object> jsonmap =  new HashMap<>();

		jsonmap.put("One","String");
		jsonmap.put("Two",new Counter());

		Object curobj;

		curobj = jsonmap.get("One");
		if (curobj instanceof String) {
			System.out.println("One is String");
		} else {
			System.out.println("One is not String");
		}

		curobj = jsonmap.get("Two");
		if (curobj instanceof Counter) {
			System.out.println("Two is Counter");
		} else {
			System.out.println("Two is not Counter");
		}

		return;
	}
}