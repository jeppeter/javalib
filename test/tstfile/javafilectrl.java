import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparsers;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

class Readfile {
	private List<String> files;
	public Readfile(Namespace ns) {
		files = ns.<String> getList("subnargs");
	}

	private int read_file(String fname) {
		BufferedReader reader = null;
		int ret = 3;
		try {
			reader = new BufferedReader( new FileReader(fname));
			int i = 0;
			String line = null;
			System.out.printf("open [%s] =================\n", fname);
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				System.out.printf("[%d] %s\n", i, line);
				i ++;
			}
			System.out.printf("close [%s] =================\n", fname);
		} catch (FileNotFoundException e) {
			return 3;
		} catch (IOException e) {
			return 4;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					return 5;
				}
			}
		}
		return 0;
	}

	public int run() {
		int ret;
		for (String fname : files) {
			ret = this.read_file(fname);
			if (ret != 0 ) {
				return ret;
			}
		}
		return 0;
	}
}


public class javafilectrl {

	private static int ReadHandle(Namespace ns) {
		Readfile r = new Readfile(ns);
		return r.run();
	}

	public static void main(String[] args) {
		int ret = 0;
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
		readparser.addArgument("subnargs").nargs("+").help("files to read");

		try {
			Namespace ns = parser.parseArgs(args);
			if (ns.getString("subcommand") == "read") {
				ret = ReadHandle(ns);
			} else {
				System.err.printf("can not handle <%s>\n", ns.getString("subcommand"));
				ret = 5;
			}
		} catch (ArgumentParserException e) {
			System.err.println("parser error " + e);
			parser.handleError(e);
			System.err.println("after handle");
			System.exit(3);
		}
		System.exit(ret);
	}
}