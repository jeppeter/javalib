
import java.util.HashMap;
import java.lang.reflect.*;

interface CallFunc {
	void FuncPtr();
}

public class functable {
	private HashMap<String,Method> m_funcs;
	private String m_name;

	public functable(String name) throws NoSuchMethodException {
		Class[] args = new Class[1];
		this.m_funcs = new HashMap<String,Method>();
		args[0] = String.class;
		this.m_funcs.put("Hello",this.getClass().getDeclaredMethod("CallHello",args));
		//this.m_funcs.put("Hello",functable.class.getMethod("CallHello",args));
		//this.m_funcs.put("Bye",functable.class.getMethod("CallBye",args));
		this.m_funcs.put("Bye",this.getClass().getDeclaredMethod("CallBye",args));
		this.m_name = name;
	}

	private Boolean CallHello(String name) {
		System.out.println(String.format("Hello %s(%s)",this.m_name,name));
		return true;
	}

	private Boolean CallBye(String name) {
		System.out.println(String.format("Bye %s(%s)",this.m_name,name));
		return true;
	}

	public void CallFunction(String name) throws IllegalAccessException,InvocationTargetException {
		CallFunc funcptr;
		Boolean bval;

		bval =(Boolean) this.m_funcs.get(name).invoke(this,(Object)name);
		System.out.println(name+" " + bval.toString());
		return;
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