import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.lang.Class;
import java.util.List;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparsers;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;


class JsonNotFoundException extends Exception {
	public JsonNotFoundException(String expr) {
		super(expr);
	}
}

class JSONEncap  {
	private String m_fname;
	private JSONObject m_objjson;
	public JSONEncap(String fname) {
		this.m_fname = fname;
	}

	public int parse() {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(this.m_fname));
			this.m_objjson = (JSONObject) obj;
		} catch (FileNotFoundException e) {
			return -3;
		} catch (IOException e) {
			return -4;
		} catch (ParseException e) {
			return -5;
		}
		return 0;
	}

	public Object getvalue(String path) throws JsonNotFoundException {
		Object obj = null;
		Object curobj = null;
		JSONObject curhash;
		String[] pathexts;
		pathexts = path.split("/");

		curobj = this.m_objjson;
		for (String curpath : pathexts) {
			if (curpath.length() == 0) {
				continue;
			}

			if (!(curobj instanceof JSONObject)) {
				throw new JsonNotFoundException("Not found " + path + " can not down (" + curpath + ") key");
			}

			curhash = (JSONObject) curobj;

			if (!curhash.containsKey(curpath)) {
				throw new JsonNotFoundException("Not found " + path + " at (" + curpath + ") key");
			}

			curobj = curhash.get(curpath);
		}

		return curobj;
	}
}


public class javaxmljson {
	private static int ReadJson(Namespace ns){
		List<String> subnargs = ns.<String> getList("subnargs");
		int idx,ret=0;
		if (subnargs.size( ) < 1) {
			System.err.println("ReadJson need file");
			return -3;
		} 

		try{
			JSONEncap jenc = new JSONEncap(subnargs.get(0));
			Object val;
			jenc.parse();
			if (subnargs.size() > 1) {
				for (idx=1;idx < subnargs.size();idx ++){
					val = jenc.getvalue(subnargs.get(idx));
					System.out.printf("[%s]=[%s]\n",subnargs.get(idx),val);
				}
			}else {
				val = jenc.getvalue("");
				System.out.printf("[]=[%s]\n",val);
			}			
		} catch(JsonNotFoundException e) {
			System.err.println(e);
			ret = -3;
		}
		return ret;
	}
	public static void main(String... args) {
		int ret = 0;
		ArgumentParser parser = ArgumentParsers.newArgumentParser("jsonxml")
		                        .defaultHelp(true)
		                        .description("json xml handler");
		Subparsers subparser = parser.addSubparsers()
		                       .title("subcommand")
		                       .dest("subcommand")
		                       .description("json xml commands")
		                       .help("json xml commands");
		Subparser readparser = subparser.addParser("readjson")
		                       .help("read file jsonpath...");
		readparser.addArgument("subnargs").nargs("+").help("file jsonpath...");

		try {
			Namespace ns = parser.parseArgs(args);
			if (ns.getString("subcommand") == "readjson") {
				ret = ReadJson(ns);
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