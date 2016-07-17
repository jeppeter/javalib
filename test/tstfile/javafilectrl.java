
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparsers;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class javafilectrl {
	public static void main(String[] args) {
		ArgumentParser parser = ArgumentParsers.newArgumentParser("filectrl")
		                  .defaultHelp(true)
		                  .description("file control handle");
		Subparsers subparser = parser.addSubparsers()
		                       .title("subcommand")
		                       .dest("subcommand")
		                       .description("file ctrl commands")
		                       .help("file controll");
		Subparser readparser = subparser.addParser("read")
		                         .help("read files...");

		 try{
		 	Namespace ns = parser.parseArgs(args);
		 } catch(ArgumentParserException e) {
		 	System.err.println("parser error");
		 	parser.handleError(e);
		 	System.exit(3);
		 }                                                                
	}
}