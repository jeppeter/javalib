

import java.lang.reflect.Method;
import namespace.NameSpaceEx;
import com.github.jeppeter.reext.ReExt;
import java.lang.ClassLoader;
import java.lang.String;
import java.lang.StackTraceElement;
import java.lang.Thread;

import java.lang.reflect.InvocationTargetException;
import java.lang.ClassNotFoundException;


class ParserException extends Exception {
	public ParserException(String message) {
		super(message);
	}
}
class Parser{
	private NameSpaceEx call_func_args(String funcname,NameSpaceEx args,Object ctx) throws ClassNotFoundException,ParserException,NoSuchMethodException,IllegalAccessException,InvocationTargetException {
		Method meth=null;
		ClassLoader clsloader = this.getClass().getClassLoader();
		String[] clss = ReExt.Split("\\.",funcname);
		int i;
		String clsname;
		String methname;
		Class ldcls;
		StackTraceElement[] stacks;
		Class[] clsobj = new Class[2];

		if (clss.length > 1) {
			clsname = "";
			for (i=0;i<(clss.length - 1);i ++) {
				if (i > 0) {
					clsname += ".";
				}
				clsname += clss[i];
			}

			/**/
			ldcls = clsloader.loadClass(clsname);
			methname = clss[clss.length - 1];
		} else {
			stacks = Thread.currentThread().getStackTrace();
			if (stacks.length < 4) {
				throw new ParserException(String.format("get stack stack 3"));
			}
			clsname = stacks[3].getClassName();

			ldcls = clsloader.loadClass(clsname);
			methname = clss[0];
		}

		/*now we should get the function*/
		clsobj[0] = NameSpaceEx.class;
		clsobj[1] = Object.class;
		meth = ldcls.getMethod(methname,clsobj);

		return (NameSpaceEx)meth.invoke(null,args,ctx);	
	}

	public NameSpaceEx call_func_args_public(String funcname,NameSpaceEx args,Object ctx) throws ClassNotFoundException,ParserException,NoSuchMethodException,IllegalAccessException,InvocationTargetException {
		return this.call_func_args(funcname,args,ctx);
	}

	Parser() {
	}

}

public class callfunc {
	public static NameSpaceEx help_handler(NameSpaceEx args,Object ctx) {
		System.out.println("Function help_handler call");
		return args;
	}

	public static NameSpaceEx go_handler(NameSpaceEx args,Object ctx) {
		System.out.println("Function go_handler call");
		return args;
	}

	public static void main(String[] params) throws ParserException {
		Parser parser = new Parser();
		NameSpaceEx args = new NameSpaceEx();

		for (String c : params) {
			try{
				parser.call_func_args_public(c,args,(Object)parser);
			}
			catch(Exception e) {
				throw new ParserException(String.format("%s:%s",e.getClass().getName(),e.toString()));
			}
		}
		return;
	}
}