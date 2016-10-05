
import java.lang.reflect.Field;
import java.util.Arrays;

public class nameset {
	static final String[] m_keywords = {"addr","name"};
	private String m_name;
	private String m_addr;
	public nameset() {
		this.m_name = "";
		this.m_addr = "";
	}
	
	public String set_value(String key,String value) throws NoSuchFieldException, IllegalAccessException {
		String oldval=null;
		String fldname=null;
		Class<?> targettype;

		if (Arrays.asList(this.m_keywords).contains(key)) {
			Field fld;
			fldname = String.format("m_%s",key);
			fld = this.getClass().getDeclaredField(fldname);
			targettype = fld.getType();
			oldval = (String)fld.get((Object) this);
			fld.set(this,value);
		}

		return oldval;
	}

	public void debug_value() {		
		System.out.println(String.format("m_name=%s",this.m_name));
		System.out.println(String.format("m_addr=%s",this.m_addr));
		return;
	}

	public void debug_fields() {
		Field[] flds;

		System.out.printf("Fields :");
		flds = this.getClass().getFields();
		for (Field f : flds) {
			System.out.printf(" "+String.format("%s",f.getName()));
		}
		System.out.println("");

		System.out.printf("Declared Fields :");
		flds = this.getClass().getDeclaredFields();
		for (Field f : flds) {
			System.out.printf(" "+String.format("%s",f.getName()));
		}
		System.out.println("");

		return;
	}

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
		nameset v = new nameset();
		String oldval;
		int i;
		v.debug_fields();
		i = 0;
		while ((i +1)< args.length)  {
			oldval = v.set_value(args[i],args[i+1]);
			System.out.println(String.format("%s (%s => %s)",args[i],oldval,args[i+1]));
			i += 2;
		}

		v.debug_value();
		return;
	}
}