
import java.util.HashMap;
import java.lang.reflect.*;

interface CallFunc {
	void FuncPtr();
}

public class functable {
	private HashMap<String,Method> m_funcs;
	private String m_name;

	public functable(String name) throws NoSuchMethodException {
		this.m_funcs = new HashMap();
		this.m_funcs.put("Hello",functable.class.getMethod("CallHello"));
		this.m_funcs.put("Bye",functable.class.getMethod("CallBye"));
		this.m_name = name;
	}

	public void CallHello() {
		System.out.println(String.format("Hello %s",this.m_name));
	}

	public void CallBye() {
		System.out.println(String.format("Bye %s",this.m_name));
	}

	public void CallFunction(String name) throws IllegalAccessException,InvocationTargetException {
		CallFunc funcptr;
		this.m_funcs.get(name).invoke(this);
	}

	public static void main(String[] args) 
	throws NoSuchMethodException,IllegalAccessException,InvocationTargetException {
		functable ptable = new functable("New");
		for (String c : args) {
			ptable.CallFunction(c);
		}
		return;
	}
}