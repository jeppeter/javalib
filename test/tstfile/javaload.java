
import java.lang.reflect.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.lang.String;
import java.io.IOException;
import java.util.Map;
import java.lang.IllegalAccessException;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparsers;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;


class StringJoin {
	private String[] m_strs;

	public StringJoin(String[] strs) {
		this.m_strs = strs;
	}

	public String toString(String exp) {
		int i;
		String retstr = "";
		if (this.m_strs == null || this.m_strs.length == 0) {
			return retstr;
		}
		for (i = 0; i < this.m_strs.length; i++) {
			retstr += this.m_strs[i];
			if (i != (this.m_strs.length - 1)) {
				retstr += exp;
			}
		}
		return retstr;
	}
}

class LoadMethodClass extends ClassLoader {
	public LoadMethodClass() {
		super();
	}

	private Class<?> __getClass(String clsname) throws ClassNotFoundException {
		byte[] bytes = null;
		String fname = clsname.replace(".", "/") + ".class";
		try {
			System.out.printf("load (%s)\n",fname);
			bytes = Files.readAllBytes(Paths.get(fname));
			System.out.printf("read (%s)\n",fname);
			return defineClass(clsname, bytes, 0, bytes.length);
		}	catch (IOException e) {
			throw new ClassNotFoundException(clsname);
		}
	}

	public Method getClassMethod(String clsmeth) throws ClassNotFoundException {
		String[] paths = clsmeth.split(".");
		String[] clss = null;
		String clsname;
		int i, cnt, j;
		Method meth = null;
		StringJoin sjoin;
		cnt = 0;
		for (i = 0; i < (paths.length); i++) {
			if (paths[i].length() > 0) {
				cnt ++;
			}
		}

		clsname = "";
		if (cnt > 0) {
			cnt --;
			clss = new String[cnt];
			j = 0;
			for (i = 0; i < paths.length; i++) {
				if (j >= cnt) {
					break;
				}
				if (paths[i].length() > 0) {
					clss[j] = paths[i];
					j ++;
				}
			}

			sjoin = new StringJoin(clss);
			clsname = sjoin.toString(".");
		}


		try {
			Class<?> cls = null;
			cls = this.__getClass(clsname);
			if (cls == null) {
				System.err.printf("(%s) null\n",clsname);
				throw new ClassNotFoundException(clsname);
			}

			meth = cls.getMethod(paths[paths.length - 1], Namespace.class, Object.class);
		}  catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.err.println(e.getSta)
			throw new ClassNotFoundException(clsmeth);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new ClassNotFoundException(clsmeth);
		}
		return meth;
	}
}

public class javaload {
	public static void main(String... args) {
		Method meth = null;
		LoadMethodClass loadcls = new LoadMethodClass();
		Namespace ns = new Namespace(null);
		System.out.println("args " + args);

		try {
			for (String s : args) {
				System.out.println("call " + s);
				meth = loadcls.getClassMethod(s);
				meth.invoke(null , ns, (Object) null);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.err);
			System.err.println(e);
			System.exit(4);
		} catch(IllegalAccessException e) {
			e.printStackTrace();
			System.err.println(e);
			System.exit(5);
		} catch(InvocationTargetException e) {
			e.printStackTrace();
			System.err.println(e);
			System.exit(6);
		}
	}
}