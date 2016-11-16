
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import net.sourceforge.argparse4j.inf.ArgumentAction;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.internal.HelpScreenException;

import java.util.Map;
import java.util.HashMap;


class CountAction implements ArgumentAction {
	@Override
	public void run(ArgumentParser parser, Argument arg,
	                Map<String, Object> attrs, String flag, Object value)
	throws ArgumentParserException {
		Integer count = 1;
		if (attrs.containsKey(arg.getDest())) {
			Object obj = attrs.get(arg.getDest());
			if (obj != null) {
				count = (Integer) obj;
				count ++;
			}
		}
		if (value != null) {
			if (flag != null && flag.length() == 2 && (value instanceof String)) {
				char shortflag = flag.charAt(1);
				String sobj = (String) value;
				int i;
				for (i=0;i<sobj.length();i++) {
					if (sobj.charAt(i) == shortflag) {
						count ++;
					}
				}
			}
		}
		attrs.put(arg.getDest(), count);
	}

	@Override
	public boolean consumeArgument() {
		return true;
	}

	@Override
	public void onAttach(Argument arg) {
	}
}

class IntAction implements ArgumentAction {
	@Override
	public void run(ArgumentParser parser, Argument arg,
	                Map<String, Object> attrs, String flag, Object value)
	throws ArgumentParserException {
		Integer count = Integer.parseInt((String) value);
		attrs.put(arg.getDest(), count);
	}

	@Override
	public boolean consumeArgument() {
		return true;
	}

	@Override
	public void onAttach(Argument arg) {
	}
}


public class appendparser {
	public static void main(String[] args) throws Exception {
		Namespace ns;
		ArgumentParser parser;
		Subparser sp;
		Subparsers sps;

		parser = ArgumentParsers.newArgumentParser("appendparser")
				.defaultHelp(true).description("append parser example");
		parser.addArgument("-v","--verbose")
				.dest("verbose")
				.action(new CountAction())
				.help("verbose mode");
		sps = parser.addSubparsers();
		sp = sps.addParser("dep").help("dep mode");
		sp.addArgument("-l","--dep-list").dest("dep_list").setDefault((Object)null).action(Arguments.append()).help("dep list");
		sp.addArgument("subnargs").metavar("subnargs").nargs("+").action(Arguments.append()).help("subnargs set");
		parser.addArgument("-p","--port")
				.dest("port")
				.action(new IntAction())
				.help("int value");
		//parser.addArgument("args").metavar("args").nargs("+").action(Arguments.append()).help("args set");

		try{
			ns = parser.parseArgs(args);
		}
		catch(HelpScreenException e) {
			return;
		}
		System.out.println(ns.toString());
	}
}