

import net.sourceforge.argparse4j.ArgumentParsers; 
import net.sourceforge.argparse4j.impl.Arguments; 
import net.sourceforge.argparse4j.inf.*; 

import java.util.List;
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
        matchparser.addArgument("subnargs").nargs("+").help("restr instr");
        matchparser.help("restr instr");
 
        
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
    	String instr,restr;
    	Pattern mpat;
    	Matcher m;
    	if (subnargs.size() < 2) {
    		System.err.printf("%s need subnargs 2\n",ns.getString("command"));
    		return -2;
    	}
    	restr = subnargs.get(0);
    	instr = subnargs.get(1);

    	mpat = Pattern.compile(restr);
    	m = mpat.matcher(instr);

    	if (m.matches()) {
    		System.out.printf("(%s) match (%s)\n",instr,restr);
    	} else {
    		System.out.printf("(%s) not match (%s)\n",instr,restr);
    	}

    	return 0;
    }

	public static void main(String... args) {
		Namespace ns = parseArgs(args);
		int ret;
		if (ns == null) {
			System.exit(1);
		}

		switch(ns.getString("command")) {
			case "match" :
				ret = match_cmd(ns);
				break;
			default:
				System.err.println("not valid command " + ns.getString("command"));
				ret = -1;
				break;
		}

		System.exit(ret);
	}
}