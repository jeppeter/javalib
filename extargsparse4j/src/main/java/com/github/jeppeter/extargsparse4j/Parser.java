package com.github.jeppeter.extargsparse4j;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import com.github.jeppeter.extargsparse4j.Priority;
import com.github.jeppeter.extargsparse4j.Key;

import com.github.jeppeter.reext.ReExt;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

class PaserBase {
	protected Subparser m_parser;
	protected List<Key> m_flags;
	protected String m_cmdname;
	protected Key m_typeclass;
	protected ParserBase(Subparsers parsers, Key keycls) {
		this.m_parser = parsers.addParser(keycls.get_string_value("cmdname"));
		this.m_flags = new ArrayList<Key>;
		this.m_cmdname = keycls.get_string_value("cmdname");
		this.m_typeclass = keycls;
	}
}

class CountAction extends ArgumentAction {
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

class IntAction extends ArgumentAction {
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

class DoubleAction extends ArgumentAction {
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

class FalseAction extends ArgumentAction {
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

class TrueAction extends ArgumentAction {
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


public class Parser  {
	private ArgumentParser m_parser;
	private Priority[] m_priorities;
	private Logger m_logger;
	private List<Key> m_flags;

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
			for (i = 0; keycls.m_flags.size(); i ++) {
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
				curparser.add(keycls);
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
			if (keycls.get_bool_object("isflag")) {
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

		this.__check_flag_insert_mustsucc(keycls, curparser);
		longopt = keycls.get_strinsg_value("longopt");
		shortopt = keycls.get_string_value("shortopt");
		optdest = keycls.get_string_value("optdest");
		helpinfo = keycls.get_string_value("helpinfo");

		if (curparser != null) {
			if (shortopt != null) {
				curparser.m_parser.addArgument(shortopt, longopt).dest(optdest).default(null).action(act).help(helpinfo);
			} else {
				curparser.m_parser.addArgument(longopt).dest(optdest).default(null).action(act).help(helpinfo);
			}
		} else {
			if (shortopt != null) {
				this.m_parser.addArgument(shortopt, longopt).dest(optdest).default(null).action(act).help(helpinfo);
			} else {
				this.m_parser.addArgument(longopt).dest(optdest).default(null).action(act).help(helpinfo);
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
		return this.__load_command_line_inner_action(prefix, keycls, curparser, Arguments.AppendArgumentAction());
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
		valid = this.__check_flag_insert(keycls, curparser)
		if (! valid) {
			return false;
		}

		if (curparser) {
			optdest = "subnargs";
		}

		helpinfo = keycls.get_string_value("helpinfo");
		if (helpinfo == null) {
			helpinfo = String.format("%s set ", optdest);
		}


		nargs = keycls.get_string_value("nargs");

		if ( !nargs.equal("0")) {
			if (curparser != null) {
				arg = curparser.addArgument(optdest);
			} else {
				arg = this.m_parser.addArgument(optdest);
			}

			arg.metavar(optdest);
			if (nargs.equal("+") || nargs.equal("*") || nargs.equal("?")) {
				arg.nargs(nargs);
			} else {
				arg.nargs(Integer.parseInt(nargs));
			}
			arg.action(Arguments.AppendArgumentAction());
			arg.help(helpinfo);
		}
		return true;
	}

	private Boolean __load_command_line_jsonfile(String prefix,Key keycls ,ParserBase curparser) {
		return this.__load_command_line_inner_action(prefix,keycls,curparser,Arguments.store());
	}

	private Boolean __load_command_line_json_added(ParserBase curparser) {
		String prefix="";
		String key = "json## json input file to get the value set ##"
		Object value = null;
		Key keycls;
		if (curparser != null) {
			prefix = curparser.m_cmdname;
		}
		keycls = new Key(prefix,key,value,true);
		return this.__load_command_line_jsonfile(prefix,keycls,curparser);
	}

	public Parser(Priority[] priority, String caption, String description, Boolean defaulthelp) {
		Priority[] defpriority = {Priority.SUB_COMMAND_JSON_SET ,
		                          Priority.COMMAND_JSON_SET , Priority.ENVIRONMENT_SET,
		                          Priority.ENV_SUB_COMMAND_JSON_SET , Priority.ENV_COMMAND_JSON_SET
		                         };

		this.m_logger = LogManager.getLogger(this.class);
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

	}

	public Parser(Priority[] priority, String caption, String description) {
		this(priority, caption, description, true);
	}

	public Parser(Priority[] priority, String caption) {
		/*now to get from the main class */
		String mainclass;
		String description;
		description = String.format("%s [OPTIONS] command ...", caption);
		this(priority, caption, description);
	}

	public Parser(Priority[] priority) {
		String mainclass;
		String description
		mainclass = get_main_class();
		this(priority, caption);
	}

	public Parser() {
		Priority[] priority = {};
		this(priority);
	}
}