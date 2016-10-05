package com.github.jeppeter.extargsparse4j;


import java.util.regex.*;
import com.github.jeppeter.extargsparse4j.KeyException;
import reext.ReExt;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import com.github.jeppeter.jsonext.JsonExt;
import java.lang.reflect.Field;

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
	private Pattern m_helpexpr;
	private Pattern m_cmdexpr;
	private Pattern m_prefixexpr;
	private Pattern m_funcexpr;
	private Patern m_flagexpr;
	private Pattern m_mustflagexpr;

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
		if (this.m_isflag) {
			assert(!this.m_iscmd);
			if (this.m_function != null ) {
				throw new KeyException(String.format("(%s) can not accept ", this.m_origkey));
			}

			if (this.m_type == "dict" && this.m_flagname != null) {
				throw new KeyException(String.format("(%s) flag can not accept dict", this.m_origkey));
			}

			if (this.m_type != TypeClass(this.m_value).get_type() && this.m_type != "count") {
				throw new KeyException(String.format("(%s) not match type(%s)", this.m_origkey, this.m_type));
			}

			if (this.m_flagname == null) {
				if (this.m_prefix == null) {
					throw new KeyException(String.format("(%s) should at least for prefix", this.m_origkey));
				}
				this.m_type = "prefix";
				if (TypeClass(this.m_value).get_type() != "dict") {
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
				if (this.m_flagname.length <= 0) {
					throw new KeyException(String.format("(%s) flagname <= 0", this.m_origkey));
				}
			}

			if (this.m_shortflag != null ) {
				if (this.m_shortflag.length > 1) {
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
			if (this.m_cmdname == null || this.m_cmdname.length == 0) {
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

	private String __get_m_field_string(String fldname) {
		Field fld;
		String innername = String.format("m_%s", fldname);
		fld = this.getClass().getDeclaredField(innername);
		return (String) fld.get((Object) this);
	}

	private void __set_m_field_string(String fldname,String value) {
		String innername = String.format("m_%s", fldname);
		fld = this.getClass().getDeclaredField(innername);
		fld.set((Object) this,(Object)value);
		return;
	}



	private void __set_flag(String prefix, String key, Object value) {
		String[] keys;
		JsonExt json = new JsonExt();
		int i;
		this.m_isflag = true;
		this.m_iscmd = false;
		this.m_origkey = key;
		json.Clone(value);
		keys = json.getKeys("/");
		if (keys == null ||  !Array.asList(keys).contains("value")) {
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
						if (TypeClass(json.getObject(keys[i])).get_type() != "string") {
							throw new KeyException(String.format("(%s).prefix type %s", this.m_origkey, TypeClass(json.getObject(keys[i])).get_type()));
						}
						newprefix = "";
						if (prefix != null && prefix.length > 0) {
							newprefix += prefix;
						}
						newprefix += json.getString(keys[i]);
						this.m_prefix = newprefix;
					} else if (keys[i] == "value") {
						if (TypeClass(json.getObject(keys[i])).get_type() == "dict") {
							throw new KeyException(String.format("(%s) %s should not be dict", this.m_origkey, keys[i]));
						}
						this.m_value = value;
						this.m_type = TypeClass(json.getObject(keys[i])).get_type();
					} else {
						throw new KeyException(String.format("(%s).%s not valid",this.m_origkey,keys[i]));
					}
				}
			}
		}

		if ((this.m_prefix == null || this.m_prefix.length == 0) && (prefix != null && prefix.length > 0)) {
			this.m_prefix = prefix;
		}
		return ;
	}


	private void __parse(String prefix, String key, Object value, boolean isflag) {
		boolean cmdmode = false;
		boolean flagmode = false;
		String flags = null;
		int i;
		ReExt ext ;
		String mstr;
		String cmd ;
		String[] retstr;
		String newprefix;
		this.m_origkey = key;
		if (this.m_origkey.contains("$")) {
			if (this.m_origkey.charAt(0) != "$") {
				throw new KeyException(String.Format("can not accept key(%s)", this.m_origkey));
			}
			for (i = 1; i < this.m_origkey.length; i++) {
				if (this.m_origkey.charAt(i) == "$") {
					throw new KeyException(String.Format("(%s) has ($) more than one", this.m_origkey));
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

			if (flags == null && this.m_origkey.charAt(0) == "$") {
				this.m_flagname = "$";
				flagmod = True;
			}

			if (flags != null) {
				if (flags.contains("|")) {
					retstr = ReExt.Split("\|", flags);
					if (retstr.length > 2 || ( retstr[1].length != 1) || ( retstr[0].length <= 1)) {
						throw new KeyException(String.Format("(%s) (%s)flag only accept (longop|l) format", this.m_origkey, flags));
					}
					this.m_flagname = retstr[0];
					this.m_shortflag = retstr[1];
				} else {
					this.m_flagname = flags ;
				}
				flagmod = True;
			}
		} else {
			this.m_mustflagexpr.FindAll(this.m_origkey);
			flags = this.m_mustflagexpr.getMatch(0, 0);
			if (flags != null) {
				if (flags.contains("|")) {
					retstr = ReExt.Split("\|", flags);
					if (retstr.length > 2 || (retstr[1].length != 1  ) || (retstr[0].length <= 1 )) {
						throw new KeyException(String.Format("(%s) (%s)flag only accept (longop|l) format", this.m_origkey, flags));
					}
					this.m_flagname = retstr[0];
					this.m_shortflag = retstr[1];
				} else {
					if (flags.length <= 1) {
						throw new KeyException(String.Format("(%s) flag must have longopt", this.m_origkey));
					}
					this.m_flagname = flags;
				}
				flagmode = True;
			} else if (this.m_origkey[0] == "$") {
				this.m_flagname  = "$";
				flagmode = True;
			}
			this.m_cmdexpr.FindAll(this.m_origkey);
			cmd = this.m_cmdexpr.getMatch(0, 0);
			if (retstr != null) {
				assert(!flagmode );
				if (cmd.contains("|"))  {
					flags = cmd;
					if (flags.contains("|")) {
						retstr = ReExt.Split("\|", flags);
						if (retstr.length > 2 || retstr[1].length != 1 || retstr[0].length <= 1) {
							throw new KeyException(String.Format('(%s) (%s)flag only accept (longop|l) format', self.__origkey, flags));
						}
						this.m_flagname = retstr[0];
						this.m_shortflag = retstr[1];
					} else {
						assert(false);
					}
					flagmode = True;
				} else {
					this.m_cmdname = cmd;
					cmdmode = True;
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
		if (prefix != null && prefix.length > 0) {
			newprefix = String.Format("%s_", prefix);
		}

		this.m_prefixexpr.FindAll(this.m_origkey);
		mstr = this.m_prefixexpr.getMatch(0, 0);
		if (mstr != null ) {
			newprefix += mstr;
			this.m_prefix = newprefix;
		} else {
			if (newprefix.length > 0) {
				this.m_prefix = newprefix;
			}
		}

		if (flagmode) {
			this.m_isflag = True;
			this.m_iscmd = False;
		}

		if (cmdmode) {
			this.m_iscmd = True;
			this.m_isflag = False;
		}

		if (!flagmode && !cmdmode) {
			this.m_isflag = True;
			this.m_iscmd = False;
		}

		this.m_value = value;

	}

	protected Key(String prefix, String key, Object value, boolean isflag) {
		this.reset();
		this.m_helpexpr = new ReExt("##([^#]+)##$", True);
		this.m_cmdexpr = new ReExt("^([^\#\<\>\+\$]+)", True);
		this.m_prefixexpr = new ReExt("\+([^\+\#\<\>\|\$ \t]+)", True);
		this.m_funcexpr = new ReExt("<([^\<\>\#\$\| \t]+)>", True);
		this.m_flagexpr = new ReExt("^([^\<\>\#\+\$ \t]+)", True);
		this.m_mustflagexpr = new ReExt("^\$([^\$\+\#\<\>]+)", True);
		this.m_origkey = key;
		this.__parse(prefix, key, value, isflag);
	}

	protected Key(String prefix, String key, Object value) {
		this(prefix, key, value, false);
	}
}