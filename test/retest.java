

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.*;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;

public class retest {

	private static Namespace parseArgs(String[] args) {
		ArgumentParser parser = ArgumentParsers.newArgumentParser("retest")
		                        .defaultHelp(true)
		                        .description("regular expression test");

		parser.addArgument("-i", "--input")
		.help("input file get");
		parser.addArgument("-o", "--output")
		.help("output input") ;

		Subparsers subparsers = parser.addSubparsers()
		                        .title("subcommands")
		                        .dest("command")
		                        .description("valid subcommands")
		                        .help("additional help");

		Subparser matchparser = subparsers.addParser("match");
		matchparser.addArgument("subnargs").nargs("+").help("restr instr...");
		matchparser.help("restr instr...");
		Subparser findallparser = subparsers.addParser("findall");
		findallparser.addArgument("subnargs").nargs("+").help("restr instr...");
		findallparser.help("restr instr...");
		Subparser imatchparser = subparsers.addParser("imatch");
		imatchparser.addArgument("subnargs").nargs("+").help("restr instr...");
		imatchparser.help("restr instr...");
		Subparser ifindallparser = subparsers.addParser("ifindall");
		ifindallparser.addArgument("subnargs").nargs("+").help("restr instr...");
		ifindallparser.help("restr instr...");



		try {
			Namespace ns = parser.parseArgs(args);
			return ns;
		} catch (ArgumentParserException e) {
			parser.handleError(e);
			return null;
		}
	}

	private static int match_cmd(Namespace ns) {
		List<String> subnargs = ns.<String>getList("subnargs");
		String instr, restr;
		Pattern mpat;
		Matcher m;
		int i;
		if (subnargs.size() < 2) {
			System.err.printf("%s restr instr...\n", ns.getString("command"));
			return -2;
		}
		restr = subnargs.get(0);
		mpat = Pattern.compile(restr);

		for (i = 1; i < subnargs.size(); i++) {
			instr = subnargs.get(i);
			m = mpat.matcher(instr);

			if (m.matches()) {
				System.out.printf("(%s) match (%s)\n", instr, restr);
			} else {
				System.out.printf("(%s) not match (%s)\n", instr, restr);
			}
		}

		return 0;
	}

	private static int findall_cmd(Namespace ns) {
		List<String> subnargs = ns.<String>getList("subnargs");
		String instr,restr;
		Pattern mpat;
		int i,j;

		if (subnargs.size() < 2) {
			System.err.printf("%s restr instr...\n", ns.getString("command"));
			return -2;
		}
		restr = subnargs.get(0);
		mpat = Pattern.compile(restr);
		for (i=1;i<subnargs.size();i++) {
			Matcher m;
			List<String> matches = new ArrayList<String>();
			instr = subnargs.get(i);
			m = mpat.matcher(instr);

			while(m.find()) {
				matches.add(m.group());
			}

			if (matches.size() > 0) {
				System.out.printf("(%s) findall(%s):",instr,restr);
				System.out.printf("[");
				for(j=0;j < matches.size();j++) {
					if (j > 0) {
						System.out.printf(",");
					}
					System.out.printf("%s",matches.get(j));
				}
				System.out.printf("]\n");
			} else {
				System.out.printf("(%s) can not findall(%s)\n",instr,restr);
			}
		}
		return 0;
	}

	private static int imatch_cmd(Namespace ns) {
		List<String> subnargs = ns.<String>getList("subnargs");
		String instr, restr;
		Pattern mpat;
		Matcher m;
		int i;
		if (subnargs.size() < 2) {
			System.err.printf("%s restr instr...\n", ns.getString("command"));
			return -2;
		}
		restr = subnargs.get(0);
		mpat = Pattern.compile(restr,Pattern.CASE_INSENSITIVE);

		for (i = 1; i < subnargs.size(); i++) {
			instr = subnargs.get(i);
			m = mpat.matcher(instr);

			if (m.matches()) {
				System.out.printf("(%s) imatch (%s)\n", instr, restr);
			} else {
				System.out.printf("(%s) not imatch (%s)\n", instr, restr);
			}
		}

		return 0;
	}

	private static int ifindall_cmd(Namespace ns) {
		List<String> subnargs = ns.<String>getList("subnargs");
		String instr,restr;
		Pattern mpat;
		int i,j;

		if (subnargs.size() < 2) {
			System.err.printf("%s restr instr...\n", ns.getString("command"));
			return -2;
		}
		restr = subnargs.get(0);
		mpat = Pattern.compile(restr,Pattern.CASE_INSENSITIVE);
		for (i=1;i<subnargs.size();i++) {
			Matcher m;
			List<String> matches = new ArrayList<String>();
			instr = subnargs.get(i);
			m = mpat.matcher(instr);

			while(m.find()) {
				matches.add(m.group());
			}

			if (matches.size() > 0) {
				System.out.printf("(%s) ifindall(%s):",instr,restr);
				System.out.printf("[");
				for(j=0;j < matches.size();j++) {
					if (j > 0) {
						System.out.printf(",");
					}
					System.out.printf("%s",matches.get(j));
				}
				System.out.printf("]\n");
			} else {
				System.out.printf("(%s) can not ifindall(%s)\n",instr,restr);
			}
		}
		return 0;
	}


	public static void main(String... args) {
		Namespace ns = parseArgs(args);
		int ret;
		if (ns == null) {
			System.exit(1);
		}

		switch (ns.getString("command")) {
		case "match" :
			ret = match_cmd(ns);
			break;
		case "findall":
			ret = findall_cmd(ns);
			break;
		case "imatch":
			ret = imatch_cmd(ns);
			break;
		case "ifindall":
			ret = ifindall_cmd(ns);
			break;
		default:
			System.err.println("not valid command " + ns.getString("command"));
			ret = -1;
			break;
		}

		System.exit(ret);
	}
}