import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;

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

class Writefile {
	private List<String> files;
	private List<String> inputlines;
	private String infile;
	public Writefile(Namespace ns) {
		infile = null;
		infile = ns.getString("read");
		files = ns.<String> getList("subnargs");
		inputlines = new ArrayList<String>();
	}

	private int write_file(String fname) {
		int ret = 0;
		File f = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			f = new File(fname);
			fw = new FileWriter(f.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			for (String s : inputlines) {
				bw.write(s + "\n");
			}
			bw.close();
			bw = null;
		} catch (IOException e) {
			System.err.printf("write [%s] error %s\n", fname, e);
			ret = 3;
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					System.err.printf("close [%s] error %s\n", fname, e);
					ret = 3;
				}
			}

			bw = null;
			fw = null;
			f = null;
		}
		return ret;
	}

	public int run() {
		List<String> lststrs;
		Scanner inscan = null;
		int ret = 0;
		int i;

		if (infile == null) {
			inscan = new Scanner(System.in);
			System.out.printf("scan from System.in\n");
		} else {
			try {
				inscan = new Scanner(new File(infile));
				System.out.printf("scan from (%s)\n",infile);
			} catch (FileNotFoundException e) {
				System.err.printf("can not open (%s) %s\n", infile, e);
				return 3;
			}
		}

		i = 0;
		while (inscan.hasNextLine()) {
			String s = inscan.nextLine();
			inputlines.add(s);
			System.out.printf("[%d] %s\n",i,s);
			i ++;
		}

		for (String fname : files) {
			ret = this.write_file(fname);
			if (ret != 0) {
				break;
			}
		}
		return ret;
	}
}


public class javafilectrl {

	private static int ReadHandle(Namespace ns) {
		Readfile r = new Readfile(ns);
		return r.run();
	}

	private static int WriteHandle(Namespace ns) {
		Writefile w = new Writefile(ns);
		return w.run();
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
		Subparser writeparser = subparser.addParser("write")
		                        .help("write files...");
		writeparser.addArgument("subnargs").nargs("+").help("files to write");
		writeparser.addArgument("-r", "--read").dest("read").setDefault(null).help("read files");

		try {
			Namespace ns = parser.parseArgs(args);
			if (ns.getString("subcommand") == "read") {
				ret = ReadHandle(ns);
			} else if (ns.getString("subcommand") == "write") {
				ret = WriteHandle(ns);
			} else {
				System.err.printf("can not handle <%s>\n", ns.getString("subcommand"));
				ret = 5;
			}
		} catch (ArgumentParserException e) {
			System.err.println("parser error " + e);
			parser.handleError(e);
			System.exit(3);
		}
		System.exit(ret);
	}
}