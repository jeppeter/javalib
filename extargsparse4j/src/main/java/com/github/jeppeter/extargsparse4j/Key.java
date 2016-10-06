package com.github.jeppeter.extargsparse4j;


import java.util.regex.*;
import com.github.jeppeter.extargsparse4j.KeyException;
import com.github.jeppeter.reext.ReExt;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import com.github.jeppeter.jsonext.JsonExt;
import java.lang.reflect.Field;
import java.util.regex.*;
import java.util.Arrays;
import java.lang.Long;
import com.github.jeppeter.jsonext.JsonExtInvalidTypeException;
import com.github.jeppeter.jsonext.JsonExtNotParsedException;
import com.github.jeppeter.jsonext.JsonExtNotFoundException;

class TypeClass {
	private String m_typename;
	public TypeClass(Object o) throws KeyException {
		if ( o instanceof String) {
			this.m_typename = "string";
		} else if (o instanceof Long) {
			this.m_typename = "long";
		} else if (o instanceof JSONObject) {
			this.m_typename = "dict";
		} else if (o instanceof JSONArray) {
			this.m_typename = "list";
		} else if (o instanceof Boolean) {
			this.m_typename = "bool";
		} else if (o instanceof Double ) {
			this.m_typename = "float";
		}  else if (o == null) {
			this.m_typename = "string";
		} else {
			throw new KeyException(String.format("unknown type %s", o.getClass().getName()));
		}
	}

	public String get_type() {
		return this.m_typename;
	}
}

public final class Key {
	private static final String[] m_flagwords = {"flagname", "helpinfo", "shortflag", "nargs"};
	private static final String[] m_flagspecial = {"value", "prefix"};
	private static final String[] m_cmdwords = {"cmdname", "function", "helpinfo"};
	private static final String[] m_otherwords_string = {"origkey", "type"};
	private static final String[] m_otherwords_bool = {"iscmd", "isflag"};
	private static final String[] m_formwords = {"longopt", "shortopt", "optdest"};
	private Object m_value;
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
	private ReExt m_helpexpr;
	private ReExt m_cmdexpr;
	private ReExt m_prefixexpr;
	private ReExt m_funcexpr;
	private ReExt m_flagexpr;
	private ReExt m_mustflagexpr;

	private void __reset() {
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

	private void __validate()  throws KeyException {
		TypeClass typecls;
		if (this.m_isflag) {
			assert(!this.m_iscmd);
			if (this.m_function != null ) {
				throw new KeyException(String.format("(%s) can not accept ", this.m_origkey));
			}

			if (this.m_type == "dict" && this.m_flagname != null) {
				throw new KeyException(String.format("(%s) flag can not accept dict", this.m_origkey));
			}

			typecls = new TypeClass(this.m_value);
			if (this.m_type != typecls.get_type() && this.m_type != "count") {
				throw new KeyException(String.format("(%s) not match type(%s)", this.m_origkey, this.m_type));
			}

			if (this.m_flagname == null) {
				if (this.m_prefix == null) {
					throw new KeyException(String.format("(%s) should at least for prefix", this.m_origkey));
				}
				this.m_type = "prefix";
				typecls = new TypeClass(this.m_value);
				if (typecls.get_type() != "dict") {
					throw new KeyException(String.format("(%s) should make dict for prefix", this.m_origkey));
				}

				if (this.m_helpinfo != null) {
					throw new KeyException(String.format("(%s) should not has helpinfo", this.m_origkey));
				}
				if (this.m_shortflag != null) {
					throw new KeyException(String.format("(%s) should not has shortflag", this.m_origkey));
				}
			} else if (this.m_flagname == "$") {
				this.m_type = "args";
				if (this.m_shortflag != null) {
					throw new KeyException(String.format("(%s) should not has shortflag", this.m_origkey));
				}
			} else {
				if (this.m_flagname.length() <= 0) {
					throw new KeyException(String.format("(%s) flagname <= 0", this.m_origkey));
				}
			}

			if (this.m_shortflag != null ) {
				if (this.m_shortflag.length() > 1) {
					throw new KeyException(String.format("(%s) shortflag > 1", this.m_origkey));
				}
			}

			if (this.m_type == "bool") {
				if (this.m_nargs != null && this.m_nargs != "0") {
					throw new KeyException(String.format("(%s) nargs != 0", this.m_origkey));
				}
				this.m_nargs = "0";
			} else if (this.m_type != "prefix" && this.m_flagname != "$" && this.m_type != "count") {
				if (this.m_flagname != "$" && this.m_nargs != "1" && this.m_nargs != null) {
					throw new KeyException(String.format("(%s) only $ accept nargs options", this.m_origkey));
				}
				this.m_nargs = "1";
			} else {
				if (this.m_flagname == "$" && this.m_nargs == null) {
					this.m_nargs = "*";
				}
			}
		} else {
			if (this.m_cmdname == null || this.m_cmdname.length() == 0) {
				throw new KeyException(String.format("(%s) no cmdnname", this.m_origkey));
			}
			if (this.m_shortflag != null) {
				throw new KeyException(String.format("(%s) has shortflag (%s)", this.m_origkey, this.m_shortflag));
			}

			if (this.m_nargs != null) {
				throw new KeyException(String.format("(%s) nargs (%s)", this.m_origkey, this.m_nargs));
			}

			if (this.m_type != "dict") {
				throw new KeyException(String.format("(%s) command must be dict", this.m_origkey));
			}
			this.m_prefix = this.m_cmdname;
			this.m_type = "command";
		}
		return;
	}

	private String __get_m_field_string(String fldname) throws NoSuchFieldException,IllegalAccessException {
		Field fld;
		String innername = String.format("m_%s", fldname);
		fld = this.getClass().getDeclaredField(innername);
		return (String) fld.get((Object) this);
	}

	private Boolean __get_m_field_bool(String fldname) throws NoSuchFieldException,IllegalAccessException {
		Field fld;
		String innername = String.format("m_%s",fldname);
		fld = this.getClass().getDeclaredField(innername);
		return (Boolean) fld.get((Object)this);
	}

	private void __set_m_field_string(String fldname,String value) throws NoSuchFieldException,IllegalAccessException {
		String innername = String.format("m_%s", fldname);
		Field fld;
		fld = this.getClass().getDeclaredField(innername);
		fld.set((Object) this,(Object)value);
		return;
	}



	private void __set_flag(String prefix, String key, Object value) throws JsonExtInvalidTypeException,JsonExtNotParsedException,JsonExtNotFoundException,NoSuchFieldException,IllegalAccessException,KeyException {
		String[] keys;
		JsonExt json = new JsonExt();
		String sobj;
		int i;
		TypeClass typecls;
		this.m_isflag = true;
		this.m_iscmd = false;
		this.m_origkey = key;
		json.Clone(value);
		keys = json.getKeys("/");
		if (keys == null ||  !Arrays.asList(keys).contains("value")) {
			this.m_value = null;
			this.m_type = "string";
		}

		if (keys != null) {
			for (i = 0; i < keys.length; i++) {
				if (Arrays.asList(this.m_flagwords).contains(keys[i])) {
					String getval = this.__get_m_field_string(keys[i]);
					if (getval != null && getval != json.getString(keys[i])) {
						throw new KeyException(String.format("(%s).%s %s != %s", this.m_origkey, keys[i], getval, json.getString(keys[i])));
					}
					this.__set_m_field_string(keys[i],json.getString(keys[i]));
				} else if (Arrays.asList(this.m_flagspecial).contains(keys[i])) {
					String newprefix;
					if (keys[i] == "prefix") {
						typecls = new TypeClass(json.getObject(keys[i]));
						if (typecls.get_type() != "string") {
							throw new KeyException(String.format("(%s).prefix type %s", this.m_origkey, typecls.get_type()));
						}
						newprefix = "";
						if (prefix != null && prefix.length() > 0) {
							newprefix += prefix;
						}
						newprefix += json.getString(keys[i]);
						this.m_prefix = newprefix;
					} else if (keys[i] == "value") {
						typecls = new TypeClass(json.getObject(keys[i]));
						if (typecls.get_type() == "dict") {
							throw new KeyException(String.format("(%s) %s should not be dict", this.m_origkey, keys[i]));
						}
						this.m_value = value;
						this.m_type = typecls.get_type();
					} else {
						throw new KeyException(String.format("(%s).%s not valid",this.m_origkey,keys[i]));
					}
				}
			}
		}

		if ((this.m_prefix == null || this.m_prefix.length() == 0) && (prefix != null && prefix.length() > 0)) {
			this.m_prefix = prefix;
		}
		return ;
	}


	private void __parse(String prefix, String key, Object value, boolean isflag) 
	          throws KeyException,JsonExtInvalidTypeException,JsonExtNotParsedException,
	          JsonExtNotFoundException,NoSuchFieldException,IllegalAccessException {
		boolean cmdmode = false;
		boolean flagmode = false;
		String flags = null;
		Long lobj;
		int i;
		ReExt ext ;
		String mstr;
		String cmd ;
		String[] retstr;
		String newprefix;
		String sobj;
		boolean valid;
		TypeClass typecls;
		this.m_origkey = key;
		if (this.m_origkey.contains("$")) {
			if (this.m_origkey.charAt(0) != '$') {
				throw new KeyException(String.format("can not accept key(%s)", this.m_origkey));
			}
			for (i = 1; i < this.m_origkey.length(); i++) {
				if (this.m_origkey.charAt(i) == '$') {
					throw new KeyException(String.format("(%s) has ($) more than one", this.m_origkey));
				}
			}
		}

		if (isflag) {
			this.m_flagexpr.FindAll(this.m_origkey);
			flags = this.m_flagexpr.getMatch(0, 0);
			if (flags == null) {
				this.m_mustflagexpr.FindAll(this.m_origkey);
				flags = this.m_mustflagexpr.getMatch(0, 0);
			}

			if (flags == null && this.m_origkey.charAt(0) == '$') {
				this.m_flagname = "$";
				flagmode = true;
			}

			if (flags != null) {
				if (flags.contains("|")) {
					retstr = ReExt.Split("\\|", flags);
					if (retstr.length > 2 || ( retstr[1].length() != 1) || ( retstr[0].length() <= 1)) {
						throw new KeyException(String.format("(%s) (%s)flag only accept (longop|l) format", this.m_origkey, flags));
					}
					this.m_flagname = retstr[0];
					this.m_shortflag = retstr[1];
				} else {
					this.m_flagname = flags ;
				}
				flagmode = true;
			}
		} else {
			this.m_mustflagexpr.FindAll(this.m_origkey);
			flags = this.m_mustflagexpr.getMatch(0, 0);
			if (flags != null) {
				if (flags.contains("|")) {
					retstr = ReExt.Split("\\|", flags);
					if (retstr.length > 2 || (retstr[1].length() != 1  ) || (retstr[0].length() <= 1 )) {
						throw new KeyException(String.format("(%s) (%s)flag only accept (longop|l) format", this.m_origkey, flags));
					}
					this.m_flagname = retstr[0];
					this.m_shortflag = retstr[1];
				} else {
					if (flags.length() <= 1) {
						throw new KeyException(String.format("(%s) flag must have longopt", this.m_origkey));
					}
					this.m_flagname = flags;
				}
				flagmode = true;
			} else if (this.m_origkey.charAt(0) == '$') {
				this.m_flagname  = "$";
				flagmode = true;
			}
			this.m_cmdexpr.FindAll(this.m_origkey);
			cmd = this.m_cmdexpr.getMatch(0, 0);
			if (cmd != null) {
				assert(!flagmode );
				if (cmd.contains("|"))  {
					flags = cmd;
					if (flags.contains("|")) {
						retstr = ReExt.Split("\\|", flags);
						if (retstr.length > 2 || retstr[1].length() != 1 || retstr[0].length() <= 1) {
							throw new KeyException(String.format("(%s) (%s)flag only accept (longop|l) format", this.m_origkey, flags));
						}
						this.m_flagname = retstr[0];
						this.m_shortflag = retstr[1];
					} else {
						assert(false);
					}
					flagmode = true;
				} else {
					this.m_cmdname = cmd;
					cmdmode = true;
				}
			}
		}

		this.m_funcexpr.FindAll(this.m_origkey);
		mstr = this.m_funcexpr.getMatch(0, 0);
		if (mstr != null) {
			this.m_function = mstr;
		}

		this.m_helpexpr.FindAll(this.m_origkey);
		mstr = this.m_helpexpr.getMatch(0, 0);
		if (mstr != null) {
			this.m_helpinfo = mstr;
		}
		newprefix = "";
		if (prefix != null && prefix.length() > 0) {
			newprefix = String.format("%s_", prefix);
		}

		this.m_prefixexpr.FindAll(this.m_origkey);
		mstr = this.m_prefixexpr.getMatch(0, 0);
		if (mstr != null ) {
			newprefix += mstr;
			this.m_prefix = newprefix;
		} else {
			if (prefix.length() > 0) {
				this.m_prefix = prefix;
			}
		}

		if (flagmode) {
			this.m_isflag = true;
			this.m_iscmd = false;
		}

		if (cmdmode) {
			this.m_iscmd = true;
			this.m_isflag = false;
		}

		if (!flagmode && !cmdmode) {
			this.m_isflag = true;
			this.m_iscmd = false;
		}

		this.m_value = value;
		typecls = new TypeClass(value);
		this.m_type = typecls.get_type();

		if (cmdmode && this.m_type != "dict") {
			flagmode = true;
			cmdmode = false;
			this.m_isflag = true;
			this.m_iscmd = false;
			this.m_flagname = this.m_cmdname;
			this.m_cmdname = null;
		}

		if (this.m_isflag && this.m_type == "string"  ) {
			sobj = (String) this.m_value;
			if (sobj == "+" && this.m_flagname != "$") {
				lobj = Long.decode(String.format("0"));
				this.m_value = (Object) lobj;
				this.m_type = "count";
				this.m_nargs = "0";
			}
		}

		if (this.m_isflag && this.m_flagname == "$" && this.m_type != "dict") {
			valid = false;
			if (this.m_type == "string") {
				sobj = (String) this.m_value;
				if (sobj == "+" || 
					sobj == "?" ||
					sobj == "*") {
					valid = true;
				}
				this.m_nargs = sobj;
				this.m_type = "count";
				this.m_value = null;

			} else if (this.m_type == "long") {
				valid = true;
				lobj = (Long) this.m_value;
				this.m_nargs = String.format("%d",lobj);
				this.m_type = "count";
				this.m_value = null;
			}

			if (!valid) {
				throw new KeyException(String.format("(%s)(%s)(%s) for $ should option dict set opt or +?* specialcase or type int",prefix,this.m_origkey,this.m_value.toString()));
			}
		}

		if (this.m_isflag && this.m_type == "dict" && this.m_flagname != null) {
			this.__set_flag(prefix,key,value);
		}
		this.__validate();
		return;
	}

	protected Key(String prefix, String key, Object value, boolean isflag) 
			throws KeyException,JsonExtInvalidTypeException,JsonExtNotParsedException,
			       JsonExtNotFoundException,NoSuchFieldException,IllegalAccessException {
		this.__reset();
		this.m_helpexpr = new ReExt("##([^#]+)##$", true);
		this.m_cmdexpr = new ReExt("^([^\\#\\<\\>\\+\\$]+)", true);
		this.m_prefixexpr = new ReExt("\\+([^\\+\\#\\<\\>\\|\\$ \\t]+)", true);
		this.m_funcexpr = new ReExt("<([^\\<\\>\\#\\$\\| \\t]+)>", true);
		this.m_flagexpr = new ReExt("^([^\\<\\>\\#\\+\\$ \\t]+)", true);
		this.m_mustflagexpr = new ReExt("^\\$([^\\$\\+\\#\\<\\>]+)", true);
		this.m_origkey = key;
		this.__parse(prefix, key, value, isflag);
	}

	protected Key(String prefix, String key, Object value) 
	      throws KeyException,JsonExtInvalidTypeException,JsonExtNotParsedException,
	                 JsonExtNotFoundException,NoSuchFieldException,IllegalAccessException {
		this(prefix, key, value, false);
	}

	protected void change_to_flag() throws KeyException {
		if (this.m_iscmd || ! this.m_isflag) {
			throw new KeyException(String.format("(%s) not cmd to change",this.m_origkey));
		}

		if (this.m_function != null) {
			throw new KeyException(String.format("(%s) has function",this.m_origkey));
		}

		assert(this.m_flagname == null) ;
		assert(this.m_shortflag == null);
		assert(this.m_cmdname != null);
		this.m_flagname = this.m_cmdname;
		this.m_cmdname = null;
		this.m_iscmd = false;
		this.m_isflag = true;
		this.__validate();
		return;
	}

	private String __get_form_string(String fldname) throws KeyException {
		String retval=null;
		Boolean bobj;
		if (! this.m_isflag || this.m_flagname == null || this.m_type == "args") {
			throw new KeyException(String.format("(%s) not valid for %s",this.m_origkey,fldname));
		}
		if (fldname == "longopt" ) {
			retval = "--";
			if (this.m_type != null && this.m_type == "bool" ) {
				bobj = (Boolean) this.m_value;
				if (bobj) {
					retval += "no-";
				}
			}

			if (this.m_prefix != null && this.m_prefix.length() > 0) {
				retval += String.format("%s-",this.m_prefix);
			}
			retval += this.m_flagname;
			retval = retval.toLowerCase(); 
			retval = retval.replace('_','-');
		} else if (fldname == "shortopt") {
			retval = null;
			if (this.m_shortflag != null) {
				retval = String.format("-%s",this.m_shortflag);
			}
		} else if (fldname == "optdest") {
			retval = "";
			if (this.m_prefix != null && this.m_prefix.length() > 0 ) {
				retval += String.format("%s_",this.m_prefix);
			}
			retval += this.m_flagname;
			retval = retval.toLowerCase();
			retval = retval.replace('-','_');
		} else {
			assert(0!=0);
		}
		return retval;
	}

	protected String get_string_value(String fldname) throws NoSuchFieldException,KeyException,IllegalAccessException {
		if (Arrays.asList(this.m_formwords).contains(fldname)) {
			return this.__get_form_string(fldname); 
		}
		if (Arrays.asList(this.m_cmdwords).contains(fldname)  ||
			Arrays.asList(this.m_flagwords).contains(fldname) ||
			Arrays.asList(this.m_flagspecial).contains(fldname) || 
			Arrays.asList(this.m_otherwords_string).contains(fldname)){
			return this.__get_m_field_string(fldname);
		}

		throw new KeyException(String.format("(%s) not valid fldname",fldname));
	}

	protected Boolean get_bool_value(String fldname) throws NoSuchFieldException,KeyException,IllegalAccessException {
		if (Arrays.asList(this.m_otherwords_bool).contains(fldname)) {
			return this.__get_m_field_bool(fldname);
		}
		throw new KeyException(String.format("(%s) not valid fldname",fldname));
	}
}