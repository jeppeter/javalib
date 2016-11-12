

import java.lang.reflect.Method;
import namespace.NameSpaceEx;
import com.github.jeppeter.reext.ReExt;

class Parser{
	private NameSpaceEx call_func_args(String funcname,NameSpaceEx args,Object ctx) {
		Method meth=null;

		return args;	
	}

	public NameSpaceEx call_func_args_public(String funcname,NameSpaceEx args,Object ctx) {
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

	public static void main(String[] params) {
		Parser parser = new Parser();
		NameSpaceEx args = new NameSpaceEx();

		for (String c : params) {
			parser.call_func_args_public(c,args,(Object)parser);
		}
		return;
	}
}