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
	protected ArgumentParser m_parser;
	protected String[] m_flags;
	protected String m_cmdname;
	protected Key m_typeclass;
}

public class Parser  {
	private ArgumentParsers m_parser;
	private Priority[] m_priorities; 
	private Logger m_logger;

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
			for (i=0;keycls.m_flags.length;i ++) {
				curcls = curparser.m_flags[i];
				if (curcls.get_string_value("flagname") != "$" &&
					keycls.get_string_value("flagname") != "$") {
					if (curcls.get_string_value("optdest") == 
						keycls.get_string_value("optdest")) {
						valid = false;
					} 
				}  else if (curcls.get_string_value("flagname") == 
					keycls.get_string_value("flagname")){
					valid = false;
				}
			}
			if (valid) {
				
			}
		} else {

		}
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

	private bool __load_command_line_string(String prefix,Key keycls,ParserBase curparser) {

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