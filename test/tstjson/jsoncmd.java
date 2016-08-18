

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;



public class jsoncmd {
	private static JSONObject parserJson(String file) {
        JSONParser parser = new JSONParser();

        try {     
            Object obj = parser.parse(new FileReader(file));

            JSONObject jsonObject =  (JSONObject) obj;
            return jsonObject;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
	}

	private static int debugjson_cmd(Namespace ns) {
		List<String> subnargs = ns.<String>getList("subnargs");
		String file;
		JSONObject objjson;
		int i;

		if (subnargs.size() < 1) {
			System.err.printf("%s file [value]...",ns.getString("command"));
			return -1;
		}
		file = subnargs.get(0);
		objjson = parserJson(file);
		if (objjson == null) {
			System.err.printf("can not parse %s\n",file);
			return -1;
		}

		if (subnargs.size() == 1) {
			System.out.printf("%s\n",objjson.toString());
		} else {
			for(i=1;i<subnargs.size();i++) {
				String k = subnargs.get(i);
				if (objjson.containsKey(k)) {
					System.out.printf("%s=%s\n",k,objjson.get(k));
				} else {
					System.err.printf("no %s\n",k);
				}
			}
		}
		return 0;
	}

	private static Namespace parseArgs(String[] args) {
		ArgumentParser parser = ArgumentParsers.newArgumentParser("jsoncmd")
		                        .defaultHelp(true)
		                        .description("json command handle");


		Subparsers subparsers = parser.addSubparsers()
		                        .title("subcommands")
		                        .dest("command")
		                        .description("valid subcommands")
		                        .help("additional help");

		Subparser djparser = subparsers.addParser("dj");
		djparser.addArgument("subnargs").nargs("+").help("file [value]...");
		djparser.help("file [value]...");

		try {
			Namespace ns = parser.parseArgs(args);
			return ns;
		} catch (ArgumentParserException e) {
			parser.handleError(e);
			return null;
		}
	}

    public static void main(String[] args) {
		Namespace ns = parseArgs(args);
		int ret;
		if (ns == null) {
			System.exit(1);
		}

		switch (ns.getString("command")) {
		case "dj" :
			ret = debugjson_cmd(ns);
			break;
		default:
			System.err.println("not valid command " + ns.getString("command"));
			ret = -1;
			break;
		}

		System.exit(ret);

    }
}