package reext;

import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;


public class ReExt  {
	private Pattern m_pattern;
	private List<Object> m_matcharray;

	public ReExt(String restr,boolean bcaseignore) {
		if (bcaseignore) {
			this.m_pattern = Pattern.compile(restr,Pattern.CASE_INSENSITIVE);
		} else {
			this.m_pattern = Pattern.compile(restr);
		}
		this.m_matcharray = new ArrayList<ArrayList<String>>();
	}

	public ReExt(String restr) {
		this(restr,false);
	}

	private boolean __mather(String instr) {
		Matcher m;
		m = this.m_pattern.matcher(instr);
		return m.matches();
	}

	public boolean Match(String instr) {
		return this.__mather(instr);
	}

	private void __findall(String instr) {
		Matcher m;
		int i;
		this.m_matcharray = new ArrayList<ArrayList<String>>();
		m = this.m_pattern.matcher(instr);
		while(m.find()){
			List<String> cc = new ArrayList<String>();
			if (m.groupCount() == 1) {
				this.m_matcharray.add(m.group(1));
			} else if (m.groupCount() > 1) {
				for (i=1;i<=m.groupCount();i++) {
					cc.add(m.group(i));
				}
				this.m_matcharray.add(cc);
			}
		}
		return;
	}

	public boolean FindAll(String instr) {
		this.__findall(instr);
		return this.__mather(instr);
	}

	public ArrayList<Object> getCount(int i ) {
		ArrayList<Object> obj = new ArrayList<Object>();
		Object mobj;
		if (this.m_matcharray.size() > i) {
			mobj = this.m_matcharray.get(i);
			if (mobj.getClass().getName() == "String") {
				obj.add(mobj);
			} else {
				obj = mobj;
			}
		}
		return obj;
	}
}