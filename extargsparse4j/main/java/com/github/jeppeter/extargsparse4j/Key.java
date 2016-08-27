package com.github.jeppeter.extargsparse4j;


import java.util.regex.*;
import com.github.jeppeter.extargsparse4j.KeyException;
import reext.ReExt;


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

	private void __parse(String prefix,String key,Object value,boolean isflag) {
		boolean cmdmode = false;
		boolean flagmode = false;
		String flags = null;
		int i;
		ReExt ext ;
		String flags;
		boolean flagmod=False;
		this.m_origkey = key;
		if (this.m_origkey.contains("$")) {
			if (this.m_origkey.charAt(0) != "$") {
				throw new KeyException(String.Format("can not accept key(%s)",this.m_origkey));
			}
			for (i=1;i<this.m_origkey.length;i++) {
				if (this.m_origkey.charAt(i) == "$") {
					thrown new KeyException(String.Format("(%s) has ($) more than one",this.m_origkey));
				}
			}
		}

		if (isflag) {
			this.m_flagexpr.FindAll(this.m_origkey);
			flags = this.m_flagexpr.getMatch(0,0);
			if (flags == null) {
				this.m_mustflagexpr.FindAll(this.m_origkey);
				flags = this.m_mustflagexpr.getMatch(0,0);
			}

			if (flags == null && this.m_origkey.charAt(0) == "$") {
				this.m_flagname = "$";
				flagmod = True;
			}

			if (flags != null) {
				
			}
		}

	}

	protected Key(String prefix,String key,Object value, boolean isflag) {
		this.reset();
		this.m_helpexpr = new ReExt("##([^#]+)##$",True);
		this.m_cmdexpr = new ReExt("^([^\#\<\>\+\$]+)",True);
		this.m_prefixexpr = new ReExt("\+([^\+\#\<\>\|\$ \t]+)",True);
		this.m_funcexpr = new ReExt("<([^\<\>\#\$\| \t]+)>",True);
		this.m_flagexpr = new ReExt("^([^\<\>\#\+\$ \t]+)",True);
		this.m_mustflagexpr = new ReExt("^\$([^\$\+\#\<\>]+)",True);
		this.m_origkey = key;
		this.__parse(prefix,key,value,isflag);
	}

	protected Key(String prefix,String key,Object value) {
		this(prefix,key,value,false);
	}
}