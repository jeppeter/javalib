package com.github.jeppeter.extargsparse4j;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import net.sourceforge.argparse4j.inf.ArgumentAction;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import com.github.jeppeter.extargsparse4j.Priority;
import com.github.jeppeter.extargsparse4j.Key;

import com.github.jeppeter.reext.ReExt;
import com.github.jeppeter.jsonext.JsonExt;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class ParserBase {
	protected Subparser m_parser;
	protected List<Key> m_flags;
	protected String m_cmdname;
	protected Key m_typeclass;
	protected ParserBase(Subparsers parsers, Key keycls) throws NoSuchFieldException,KeyException,IllegalAccessException {
		this.m_parser = parsers.addParser(keycls.get_string_value("cmdname"));
		this.m_flags = new ArrayList<Key>();
		this.m_cmdname = keycls.get_string_value("cmdname");
		this.m_typeclass = keycls;
	}
}

class CountAction implements ArgumentAction {
	@Override
	public void run(ArgumentParser parser, Argument arg,
	                Map<String, Object> attrs, String flag, Object value)
	throws ArgumentParserException {
		Integer count = 0;
		if (attrs.containsKey(arg.getDest())) {
			Object obj = attrs.get(arg.getDest());
			if (obj != null) {
				count = (Integer) obj;
				count ++;
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

class DoubleAction implements ArgumentAction {
	@Override
	public void run(ArgumentParser parser, Argument arg,
	                Map<String, Object> attrs, String flag, Object value)
	throws ArgumentParserException {
		Double count = Double.parseDouble((String)value);
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

class FalseAction implements ArgumentAction {
	@Override
	public void run(ArgumentParser parser, Argument arg,
	                Map<String, Object> attrs, String flag, Object value)
	throws ArgumentParserException {
		Boolean bobj = false;
		attrs.put(arg.getDest(), bobj);
	}

	@Override
	public boolean consumeArgument() {
		return false;
	}

	@Override
	public void onAttach(Argument arg) {
	}
}

class TrueAction implements ArgumentAction {
	@Override
	public void run(ArgumentParser parser, Argument arg,
	                Map<String, Object> attrs, String flag, Object value)
	throws ArgumentParserException {
		Boolean bobj = true;
		attrs.put(arg.getDest(), bobj);
	}

	@Override
	public boolean consumeArgument() {
		return false;
	}

	@Override
	public void onAttach(Argument arg) {
	}
}

class ListAction implements ArgumentAction {
	@Override
	public void run(ArgumentParser parser, Argument arg,
	                Map<String, Object> attrs, String flag, Object value)
	throws ArgumentParserException {
		List<String> lobj;
		Object obj;
		obj = attrs.get(arg.getDest());
		if (obj == null) {
			lobj = new ArrayList<String>();
		} else {
			lobj = (List<String>) obj;
		}
		lobj.add((String)value); 
		attrs.put(arg.getDest(), lobj);
	}

	@Override
	public boolean consumeArgument() {
		return true;
	}

	@Override
	public void onAttach(Argument arg) {
	}
}

public class Parser  {
	private ArgumentParser m_parser;
	private Subparsers m_subparsers;
	private Priority[] m_priorities;
	private Logger m_logger;
	private List<Key> m_flags;
	private HashMap<String,Method> m_functable;
	private List<ParserBase> m_cmdparsers;
	private HashMap<String,Method> m_argsettable;

	private static String get_main_class() {
		String command = System.getProperty("sun.java.command");
		String[] names;
		String mainclass;
		names = ReExt.Split("\\s+", command);
		mainclass = "Parser";
		if (names.length > 0) {
			if (names[0].contains(".")) {
				names = ReExt.Split("\\.", names[0]);
				/*get last one*/
				mainclass = names[(names.length - 1)];
			} else {
				mainclass = names[0];
			}
		}
		return mainclass;
	}

	private Boolean __check_flag_insert(Key keycls, ParserBase curparser) {
		Boolean valid = false;
		int i;
		Key curcls;
		if (curparser != null) {
			valid = true;
			for (i = 0; i < curparser.m_flags.size(); i ++) {
				curcls = curparser.m_flags.get(i);
				if (curcls.get_string_value("flagname") != "$" &&
				        keycls.get_string_value("flagname") != "$") {
					if (curcls.get_string_value("optdest") ==
					        keycls.get_string_value("optdest")) {
						valid = false;
						break;
					}
				}  else if (curcls.get_string_value("flagname") ==
				            keycls.get_string_value("flagname")) {
					valid = false;
					break;
				}
			}
			if (valid) {
				curparser.m_flags.add(keycls);
			}
		} else {
			valid = true;
			for (i = 0; i < this.m_flags.size(); i++) {
				curcls = this.m_flags.get(i);
				if (curcls.get_string_value("flagname") != "$" &&
				        keycls.get_string_value("flagname") != "$") {
					if (curcls.get_string_value("optdest") ==
					        keycls.get_string_value("optdest")) {
						valid = false;
						break;
					}
				} else if (curcls.get_string_value("flagname") ==
				           keycls.get_string_value("flagname")) {
					valid = false;
					break;
				}
			}
			if (valid) {
				this.m_flags.add(keycls);
			}
		}
		return valid;
	}

	private Boolean __check_flag_insert_mustsucc(Key keycls, ParserBase curparser) throws ParserException {
		Boolean valid;
		valid = this.__check_flag_insert(keycls, curparser);
		if (! valid ) {
			String cmdname;
			cmdname = "main";
			if (curparser != null) {
				cmdname = curparser.m_cmdname;
			}
			throw new ParserException(String.format("(%s) already in (%s)", keycls.get_string_value("flagname"), cmdname));
		}
		return valid;
	}

	private String __get_help_info(Key keycls) {
		String helpinfo = "", s;
		String typestr;
		Boolean bobj;
		String sobj;
		Object obj;
		typestr = keycls.get_string_value("type");

		if (typestr == "bool") {
			bobj = (Boolean)keycls.get_object_value("value");
			if (bobj) {
				helpinfo += String.format("%s set false default(True)", keycls.get_string_value("optdest"));
			} else {
				helpinfo += String.format("%s set true default(False)", keycls.get_string_value("optdest"));
			}
		} else if (typestr == "string") {
			sobj = (String) keycls.get_object_value("value");
			if (sobj != null) {
				helpinfo += String.format("%s set default(%s)", keycls.get_string_value("optdest"), sobj);
			} else {
				helpinfo += String.format("%s set default(null)", keycls.get_string_value("optdest"));
			}
		} else {
			if (keycls.get_bool_value("isflag")) {
				obj = keycls.get_object_value("value");
				helpinfo += String.format("%s set default(%s)", keycls.get_string_value("optdest"), obj.toString());
			} else {
				helpinfo += String.format("%s command exec", keycls.get_string_value("optdest"));
			}
		}

		if (keycls.get_string_value("helpinfo") != null)  {
			helpinfo = keycls.get_string_value("helpinfo");
		}

		return helpinfo;
	}

	private Boolean __load_command_line_inner_action(String prefix, Key keycls, ParserBase curparser, ArgumentAction act) {
		String longopt, shortopt, optdest, helpinfo;
		Argument thisarg;

		this.__check_flag_insert_mustsucc(keycls, curparser);
		longopt = keycls.get_string_value("longopt");
		shortopt = keycls.get_string_value("shortopt");
		optdest = keycls.get_string_value("optdest");
		helpinfo = keycls.get_string_value("helpinfo");

		if (curparser != null) {
			if (shortopt != null) {
				curparser.m_parser.addArgument(shortopt,longopt).dest(optdest).setDefault((Object)null).action(act).help(helpinfo);
			} else {
				curparser.m_parser.addArgument(longopt).dest(optdest).setDefault((Object)null).action(act).help(helpinfo);
			}
		} else {
			if (shortopt != null) {
				this.m_parser.addArgument(shortopt, longopt).dest(optdest).setDefault((Object)null).action(act).help(helpinfo);
			} else {
				this.m_parser.addArgument(longopt).dest(optdest).setDefault((Object)null).action(act).help(helpinfo);
			}
		}
		return true;
	}

	private Boolean __load_command_line_string(String prefix, Key keycls, ParserBase curparser) throws ParserException {
		return this.__load_command_line_inner_action(prefix, keycls, curparser, Arguments.store());
	}

	private Boolean __load_command_line_count(String prefix, Key keycls, ParserBase curparser) throws ParserException {
		return this.__load_command_line_inner_action(prefix, keycls, curparser, new CountAction());
	}

	private Boolean __load_command_line_int(String prefix, Key keycls, ParserBase curparser) throws ParserException {
		return this.__load_command_line_inner_action(prefix, keycls, curparser, new IntAction());
	}

	private Boolean __load_command_line_float(String prefix, Key keycls, ParserBase curparser)   {
		return this.__load_command_line_inner_action(prefix, keycls, curparser, new DoubleAction());
	}

	private Boolean __load_command_line_list(String prefix, Key keycls, ParserBase curparser) {
		return this.__load_command_line_inner_action(prefix, keycls, curparser, Arguments.append());
	}

	private Boolean __load_command_line_bool(String prefix, Key keycls, ParserBase curparser) {
		Boolean bobj = (Boolean) keycls.get_object_value("value");
		if (bobj) {
			return this.__load_command_line_inner_action(prefix, keycls, curparser, new TrueAction());
		}
		return this.__load_command_line_inner_action(prefix, keycls, curparser, new FalseAction());
	}

	private Boolean __load_command_line_args(String prefix, Key keycls, ParserBase curparser) {
		Boolean valid;
		String optdest = "args", helpinfo,nargs;
		Argument arg;
		valid = this.__check_flag_insert(keycls, curparser);
		if (! valid) {
			return false;
		}

		if (curparser != null) {
			optdest = "subnargs";
		}

		helpinfo = keycls.get_string_value("helpinfo");
		if (helpinfo == null) {
			helpinfo = String.format("%s set ", optdest);
		}


		nargs = keycls.get_string_value("nargs");

		if ( ! nargs.equals("0")) {
			if (curparser != null) {
				arg = curparser.m_parser.addArgument(optdest);
			} else {
				arg = this.m_parser.addArgument(optdest);
			}

			arg.metavar(optdest);
			if (nargs.equals("+") || nargs.equals("*") || nargs.equals("?")) {
				arg.nargs(nargs);
			} else {
				arg.nargs(Integer.parseInt(nargs));
			}
			arg.action(Arguments.append());
			arg.help(helpinfo);
		}
		return true;
	}

	private Boolean __load_command_line_jsonfile(String prefix,Key keycls ,ParserBase curparser) {
		return this.__load_command_line_inner_action(prefix,keycls,curparser,Arguments.store());
	}

	private Boolean __load_command_line_json_added(ParserBase curparser) {
		String prefix="";
		String key = "json## json input file to get the value set ##";
		Object value = null;
		Key keycls;
		if (curparser != null) {
			prefix = curparser.m_cmdname;
		}
		keycls = new Key(prefix,key,value,true);
		return this.__load_command_line_jsonfile(prefix,keycls,curparser);
	}

	private ParserBase __find_subparser_inner(String cmdname) {
		int i;
		ParserBase parsebase;
		if (this.m_cmdparsers == null) {
			return null;
		}
		for (i=0;i<this.m_cmdparsers.size();i++) {
			parsebase = this.m_cmdparsers.get(i);
			if (parsebase.m_cmdname.equals(cmdname) ) {
				return parsebase;
			}
		}
		return null;
	}

	private ParserBase __get_subparser_inner(Key keycls) {
		ParserBase cmdparser=null;
		String helpinfo;
		ArgumentParser parser=null;

		cmdparser = this.__find_subparser_inner(keycls.get_string_value("cmdname"));
		if (cmdparser != null) {
			return cmdparser;
		}

		if (this.m_subparsers == null) {
			this.m_subparsers = this.m_parser.addSubparsers();
		}

		helpinfo = this.__get_help_info(keycls);
		cmdparser = new ParserBase(this.m_subparsers,keycls);
		cmdparser.m_parser.help(helpinfo);

		if (this.m_cmdparsers == null) {
			this.m_cmdparsers = new ArrayList<ParserBase>();
		}
		this.m_cmdparsers.add(cmdparser);
		return cmdparser;
	}

	private Boolean __load_command_subparser(String prefix,Key keycls,ParserBase curparser) {
		Object vobj;
		ParserBase nextparser=null;
		if (curparser != null) {
			throw new ParserException(String.format("(%s) can not make command recursively",keycls.get_string_value("origkey")));
		}

		vobj = keycls.get_object_value("value");
		if (!(vobj instanceof JSONObject)){
			throw new ParserException(String.format("(%s) must be value dict",keycls.get_string_value("origkey")));
		}

		nextparser = this.__get_subparser_inner(keycls);
		this.__load_command_line_inner(keycls.get_string_value("prefix"),(JSONObject)vobj,nextparser);
		return true;
	}

	public Parser(Priority[] priority, String caption, String description, Boolean defaulthelp) {
		Priority[] defpriority = {Priority.SUB_COMMAND_JSON_SET ,
		                          Priority.COMMAND_JSON_SET , Priority.ENVIRONMENT_SET,
		                          Priority.ENV_SUB_COMMAND_JSON_SET , Priority.ENV_COMMAND_JSON_SET
		                         };

		this.m_logger = LogManager.getLogger(this.getClass().getName());
		if (priority.length == 0 ) {
			this.m_priorities = defpriority;
		} else {
			this.m_priorities = priority;
		}

		this.m_logger.info("priority (%s) m_priorities (%s) caption(%s) description (%s) help %s",
		                   priority, this.m_priorities, caption, description, defaulthelp ? "True" : "False");
		this.m_parser = ArgumentParsers.newArgumentParser(caption)
		                .defaultHelp(defaulthelp)
		                .description(description);
		this.m_functable = new HashMap();
		this.m_functable.put("string",this.getClass().getMethod("__load_command_line_string"));
		this.m_functable.put("unicode",this.getClass().getMethod("__load_command_line_string"));
		this.m_functable.put("int",this.getClass().getMethod("__load_command_line_int"));
		this.m_functable.put("float",this.getClass().getMethod("__load_command_line_float"));
		this.m_functable.put("list",this.getClass().getMethod("__load_command_line_list"));
		this.m_functable.put("bool",this.getClass().getMethod("__load_command_line_bool"));
		this.m_functable.put("args",this.getClass().getMethod("__load_command_line_args"));
		this.m_subparsers = null;
		this.m_cmdparsers = null;

	}

	public Parser(Priority[] priority, String caption, String description) {
		this(priority, caption, description, true);
	}

	public Parser(Priority[] priority, String caption) {
		this(priority, caption, String.format("%s [OPTIONS] command ...",caption));
	}

	public Parser(Priority[] priority) {
		this(priority, "",get_main_class());
	}

	public Parser() {
		this(new Priority[] {});
	}

	private Namespace __load_jsonvalue(Namespace args,String prefix,Object jsonvalue,List<Key> flagarray) {
		JSONObject jobj = null;

		if (! (jsonvalue instanceof JSONObject)) {
			throw new ParserException(String.format("value type (%s) not JSONObject",jsonvalue.getClass().getName()));
		}
		
	}

	private Namespace __load_jsonfile(Namespace args,String subcmd,String jsonfile,ParserBase curparser) {
		String prefix="";
		List<Key> flagarray=null;
		JsonExt jext;
		Object jsonvalue=null;
		assert(jsonfile != null);
		if (subcmd != null) {
			prefix += subcmd;
		}

		flagarray = this.m_flags;
		if (curparser != null) {
			flagarray = curparser.m_flags;
		}

		/*now we read file and give the jobs*/
		jext = new JsonExt();
		jext.parseFile(jsonfile);
		return this.__load_jsonvalue(args,prefix,jsonvalue,flagarray);
	}

	private Namespace __parse_sub_command_json_set(Namespace args) {
		if (this.m_subparsers != null && args.getString("subcommand") != null) {
			String jsondest = String.format("%s_json",args.getString("subcommand"));
			ParserBase curparser = this.__find_subparser_inner(args.getString("subcommand"));
			String jsonfile;
			assert(curparser != null);
			jsonfile = args.getString(jsondest);
			if (jsonfile != null) {
				args = this.__load_jsonfile(args,args.getString("subcommand"),jsonfile,curparser);
			}
		}
		return args;
	}

	private Namespace __parse_command_json_set(Namespace args) {
		if (args.getString("json") != null) {
			String jsonfile = args.getString("json");
			if (jsonfile != null) {
				args = this.__load_jsonfile(args,"",jsonfile,null );
			}
		}
		return args;
	}

	private Namespace __parse_environment_set(Namespace args) {
		return this.__set_environ_value(args);
	}

	private Namespace __parse_env_subcommand_json_set(Namespace args) {
		if (this.m_subparsers != null && args.getString("subcommand") != null) {
			String jsondest = String.format("%s_json",args.getString("subcommand"));
			ParserBase curparser = this.__find_subparser_inner(args.getString("subcommand"));
			String jsonfile;
			assert(curparser != null);
			jsondest = jsondest.replace('-','_');
			jsondest = jsondest.toUpperCase();
			jsonfile = System.getenv(jsondest);
			if (jsonfile != null) {
				args = this.__load_jsonfile(args,args.getString("subcommand"),jsonfile,curparser);
			}
		}
		return args;
	}

	private Namespace __parse_env_command_json_set(Namespace args) {
		String jsonfile;
		jsonfile = System.getenv("EXTARGSPARSE_JSON");
		if (jsonfile != null) {
			args = this.__load_jsonfile(args,"",jsonfile,null);
		}
		return args;
	}

	private Namespace __set_environ_value_inner(Namespace args,String prefix,List<Key> flagarray ) {
		int i;
		Key keycls;
		for (i=0;i<flagarray.size();i++) {
			keycls = flagarray.get(i);
			if (keycls.get_bool_object("isflag") && 
				!keycls.get_string_value("type").equals("prefix") &&
				!keycls.get_string_value("type").equals("args")) {
				String optdest;
				String oldopt;
				String val;
				optdest = keycls.get_string_value("optdest");
				oldopt = optdest;
				if (args.get(oldopt) != null) {
					continue;
				}
				if (optdest.indexOf('_') < 0) {
					optdest = String.format("EXTARGS_%s",optdest);
				}
				val = System.getenv(optdest);
				if (val != null) {
					if (keycls.get_string_value("type") == "string") {
						args.put(oldopt,(Object)val);
					} else if (keycls.get_string_value("type") == "bool") {
						Boolean bval;
						if (val.toLowerCase() == "true") {
							bval = true;
							args.put(oldopt,bval);
						} else if (val.toLowerCase() == "false") {
							bval = false;
							args.put(oldopt,bval);
						}
					} else if (keycls.get_string_value("type") == "list") {
						String jsonstr = String.format("{ \"dummy\" : %s }",(String)val);
						JsonExt jsonext = new JsonExt();
						Object jobj;
						JSONArray jarr;
						List<String> lobj;
						int jidx;
						jsonext.parseString(jsonstr);

						jobj = jsonext.getObject("/dummy");
						if ( jobj instanceof JSONArray) {
							lobj = new ArrayList<String>();
							for (jidx = 0;jidx < jarr.length();jidx ++) {
								obj = jarr.get(i);
								if (!(obj instanceof String)){
									throw new ParserException(String.format("%s(%s)[%d] not string object",optdest,(String)val,i));
								}
								lobj.add((String)obj);
							}
						} else {
							throw new ParserException(String.format("%s(%s) not valid list",optdest,(String)val));
						}
						args.put(oldopt,lobj);
					} else if (keycls.get_string_value("type") == "int" ) {
						args.put(oldopt,Integer.parseInt((String)val));
					} else if (keycls.get_string_value("type") == "float") {
						args.put(oldopt,Float.parseFloat((String)val));
					} else {
						throw new ParserException(String.format("unknown type(%s) for (%s)",keycls.get_string_value("type"),oldopt));
					}
				}
			}
		}

		return args;
	}

	private void __load_command_line_inner(String prefix,Object obj,ParserBase curparser) {
		JSONObject jobj;
		Object val;
		String[] keys;
		Method meth;
		String type;
		Key keycls;
		int i;
		Boolean valid;
		this.__load_command_line_json_added(curparser);
		jobj = (JSONObject) obj;
		keys = jobj.names();
		for (i=0;i<keys.length();i++){
			val = jobj.get(keys[i]);
			if (curparser != null) {
				this.m_logger.info("%s , %s , %s , True",prefix,keys[i],val.toString());
				keycls = new Key(prefix,keys[i],val,true);
			} else {
				this.m_logger.info("%s , %s , %s , False",prefix,keys[i],val.toString());
				keycls = new Key(prefix,keys[i],val,false);
			}

			meth = this.m_functable.get(keycls.get_string_value("type"));
			valid = meth.invoke(this,prefix,keycls,curparser);
			if (! valid) {
				throw new ParserException(String.format("can not add %s %s",keys[i],val.toString()));
			}
		}
		return;
	}

	public void load_command_line(Object obj) {
		if (!(obj instanceof JSONObject)) {
			throw new ParserException(String.format("obj is not JSONObject"));
		}
		this.__load_command_line_inner("",obj,None);
		return;
	}

	public void load_command_line_string(String str) {
		Object obj;
		JsonExt jext = new JsonExt();
		jext.parseString(str);
		obj = jext.getObject("/");
		return this.load_command_line(obj);

	}

}