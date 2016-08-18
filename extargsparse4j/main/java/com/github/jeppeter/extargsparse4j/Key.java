package com.github.jeppeter.extargsparse4j;


import java.util.regex.*;


public final class Key {
	private String m_value;
	private String m_prefix;
	private String m_flagname;
	private String m_helpinfo;
	private String m_shortflag;
	private String m_nargs;
	private String m_cmdname;
	private String m_function;
	private String m_origkey;
	private boolean m_iscmd;
	private boolean m_isflag;
	private String m_type;
	private Pattern m_helpexpr;
	private Pattern m_cmdexpr;
	private Pattern m_prefixexpr;
	private Pattern m_funcexpr;
	private Patern m_flagexpr;
	private Pattern m_mustflagexpr;

	private void reset() {
		this.m_value = null;
		this.m_prefix = "";
		this.m_flagname = null;
		this.m_helpinfo = null;
		this.m_shortflag = null;
		this.m_nargs = null;
		this.m_cmdname = null;
		this.m_function = null;
		this.m_origkey = null;
		this.m_iscmd = false;
		this.m_isflag = false;
		this.m_type = null;
		return;
	}

	protected Key(String prefix,String key,Object value, boolean isflag) {
		this.reset();
		this.m_helpexpr = Pattern.compile("##([^#]+)##$",Pattern.CASE_INSENSITIVE);
		this.m_cmdexpr = Pattern.compile("^([^\#\<\>\+\$]+)",Pattern.CASE_INSENSITIVE);
	}

	protected Key(String prefix,String key,Object value) {
		this(prefix,key,value,false);
	}
}