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
	protected ParserBase(Subparsers parsers,Key keycls) {
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
         Integer count =0;
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

public class Parser  {
	private ArgumentParser m_parser;
	private Priority[] m_priorities; 
	private Logger m_logger;
	private List<Key> m_flags;

	private static String get_main_class() {
		String command = System.getProperty("sun.java.command");		
		String[] names;
		String mainclass;
		names = ReExt.Split("\\s+",command);
		mainclass = "Parser";
		if (names.length > 0) {
			if (names[0].contains(".")) {
				names = ReExt.Split("\\.",names[0]);
				/*get last one*/
				mainclass = names[(names.length - 1)];
			} else {
				mainclass = names[0];
			}
		}
		return mainclass;
	}

	private Boolean __check_flag_insert(Key keycls,ParserBase curparser) {
		Boolean valid = false;
		int i;
		Key curcls;
		if (curparser != null) {
			valid = true;
			for (i=0;keycls.m_flags.size();i ++) {
				curcls = curparser.m_flags.get(i);
				if (curcls.get_string_value("flagname") != "$" &&
					keycls.get_string_value("flagname") != "$") {
					if (curcls.get_string_value("optdest") == 
						keycls.get_string_value("optdest")) {
						valid = false;
					break;
					} 
				}  else if (curcls.get_string_value("flagname") == 
					keycls.get_string_value("flagname")){
					valid = false;
					break;
				}
			}
			if (valid) {
				curparser.add(keycls);
			}
		} else {
			valid = true;
			for (i=0;i<this.m_flags.size();i++) {
				curcls = this.m_flags.get(i);
				if (curcls.get_string_value("flagname") != "$" &&
					keycls.get_string_value("flagname") != "$") {
					if (curcls.get_string_value("optdest") == 
						keycls.get_string_value("optdest")) {
						valid = false;
						break;
					}
				}else if (curcls.get_string_value("flagname") == 
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

	private Boolean __check_flag_insert_mustsucc(Key keycls,ParserBase curparser) throws ParserException {
		Boolean valid;
		valid = this.__check_flag_insert(keycls,curparser);
		if (! valid ) {
			String cmdname;
			cmdname = "main";
			if (curparser != null) {
				cmdname = curparser.m_cmdname;
			}
			throw new ParserException(String.format("(%s) already in (%s)",keycls.get_string_value("flagname"),cmdname));
		}
		return valid;
	}

	private String __get_help_info(Key keycls) {
		String helpinfo="",s;
		String typestr;
		Boolean bobj;
		String sobj;
		Object obj;
		typestr = keycls.get_string_value("type");

		if (typestr == "bool") {
			bobj = (Boolean)keycls.get_object_value("value");
			if (bobj) {
				helpinfo += String.format("%s set false default(True)",keycls.get_string_value("optdest"));
			} else {
				helpinfo += String.format("%s set true default(False)",keycls.get_string_value("optdest"));
			}
		} else if (typestr == "string") {
			sobj = (String) keycls.get_object_value("value");
			if (sobj != null) {
				helpinfo += String.format("%s set default(%s)",keycls.get_string_value("optdest"),sobj);
			} else {
				helpinfo += String.format("%s set default(null)",keycls.get_string_value("optdest"));
			}
		} else {
			if (keycls.get_bool_object("isflag")) {
				obj = keycls.get_object_value("value");
				helpinfo += String.format("%s set default(%s)",keycls.get_string_value("optdest"),obj.toString());
			} else {
				helpinfo += String.format("%s command exec",keycls.get_string_value("optdest"));
			}
		}

		if (keycls.get_string_value("helpinfo")!= null)  {
			helpinfo = keycls.get_string_value("helpinfo");
		}

		return helpinfo;
	}

	private Boolean __load_command_line_string(String prefix,Key keycls,ParserBase curparser) throws ParserException {
		String longopt,shortopt,optdest,helpinfo;
		Subparser sparser = null;

		this.__check_flag_insert_mustsucc(keycls,curparser);
		longopt = keycls.get_strinsg_value("longopt");
		shortopt = keycls.get_string_value("shortopt");
		optdest = keycls.get_string_value("optdest");
		helpinfo = keycls.get_string_value("helpinfo");

		if (curparser != null) {
			if (shortopt != null) {
				curparser.m_parser.addArgument(shortopt,longopt).dest(optdest).default(null).action(Arguments.store()).help(helpinfo);
			} else {
				curparser.m_parser.addArgument(longopt).dest(optdest).default(null).action(Arguments.store()).help(helpinfo);
			}
		} else {
			if (shortopt != null) {
				this.m_parser.addArgument(shortopt,longopt).dest(optdest).default(null).action(Arguments.store()).help(helpinfo);
			} else {
				this.m_parser.addArgument(longopt).dest(optdest).default(null).action(Arguments.store()).help(helpinfo);
			}
		}
		return true;
	}

	private Boolean __load_command_line_count(String prefix,Key keycls,ParserBase curparser) throws ParserException {
		String longopt,shortopt,optdest,helpinfo;
		Subparser sparser = null;

		this.__check_flag_insert_mustsucc(keycls,curparser);
		longopt = keycls.get_strinsg_value("longopt");
		shortopt = keycls.get_string_value("shortopt");
		optdest = keycls.get_string_value("optdest");
		helpinfo = keycls.get_string_value("helpinfo");

		if (curparser != null) {
			if (shortopt != null) {
				curparser.m_parser.addArgument(shortopt,longopt).dest(optdest).action(new CountAction()).default(null).help(helpinfo);
			} else {
				curparser.m_parser.addArgument(longopt).dest(optdest).action(new CountAction()).default(null).help(helpinfo);
			}
		} else {
			if (shortopt != null) {
				this.m_parser.addArgument(shortopt,longopt).dest(optdest).default(null).action(new CountAction()).help(helpinfo);
			} else {
				this.m_parser.addArgument(longopt).dest(optdest).default(null).action(new CountAction()).help(helpinfo);
			}
		}
		return true;		
	}

	private Boolean __load_command_line_int(String prefix,Key keycls,ParserBase curparser) throws ParserException {
		String longopt,shortopt,optdest,helpinfo;
		Subparser sparser = null;

		this.__check_flag_insert_mustsucc(keycls,curparser);
		longopt = keycls.get_strinsg_value("longopt");
		shortopt = keycls.get_string_value("shortopt");
		optdest = keycls.get_string_value("optdest");
		helpinfo = keycls.get_string_value("helpinfo");

		if (curparser != null) {
			if (shortopt != null) {
				curparser.m_parser.addArgument(shortopt,longopt).dest(optdest).action(Arguments.count()).default(null).help(helpinfo);
			} else {
				curparser.m_parser.addArgument(longopt).dest(optdest).default(null).action(Arguments.count()).help(helpinfo);
			}
		} else {
			if (shortopt != null) {
				this.m_parser.addArgument(shortopt,longopt).dest(optdest).default(null).action(Arguments.count()).help(helpinfo);
			} else {
				this.m_parser.addArgument(longopt).dest(optdest).default(null).action(Arguments.count()).help(helpinfo);
			}
		}
		return true;		
	}

	public Parser(Priority[] priority,String caption,String description,Boolean defaulthelp) {
		Priority[] defpriority = {Priority.SUB_COMMAND_JSON_SET ,
		 Priority.COMMAND_JSON_SET ,Priority.ENVIRONMENT_SET,
		 Priority.ENV_SUB_COMMAND_JSON_SET , Priority.ENV_COMMAND_JSON_SET };

		 this.m_logger = LogManager.getLogger(this.class);
		 if (priority.length == 0 ) {
		 	this.m_priorities = defpriority;
		 } else {
		 	this.m_priorities = priority;
		 }

		 this.m_logger.info("priority (%s) m_priorities (%s) caption(%s) description (%s) help %s",
		 	priority,this.m_priorities,caption,description,defaulthelp ? "True" : "False");
		 this.m_parser = ArgumentParsers.newArgumentParser(caption)
		               .defaultHelp(defaulthelp)
		               .description(description);

	}

	public Parser(Priority[] priority,String caption,String description) {
		this(priority,caption,description,true);
	}

	public Parser(Priority[] priority,String caption) {
		/*now to get from the main class */ 
		String mainclass;
		String description;
		description = String.format("%s [OPTIONS] command ...",caption);
		this(priority,caption,description);
	}

	public Parser(Priority[] priority) {
		String mainclass;
		String description
		mainclass = get_main_class();
		this(priority,caption);
	}

	public Parser() {
		Priority[] priority = {};
		 this(priority);
	}
}