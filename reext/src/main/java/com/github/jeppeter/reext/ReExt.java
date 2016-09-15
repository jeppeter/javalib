package com.github.jeppeter.reext;

import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked") 
public class ReExt  {
    private Pattern m_pattern;
    private List<Object> m_matcharray;

    public static String[] Split(String restr,String instr,int max) {
        String[] retstr ;
        if (max > 0) {
            retstr = instr.split(restr,max);
        } else {
            retstr = instr.split(restr);
        }
        return retstr;
    }

    public static String[] Split(String restr,String instr) {
        return Split(restr,instr,-1);
    }

    public ReExt(String restr, boolean bcaseignore) {
        if (bcaseignore) {
            this.m_pattern = Pattern.compile(restr, Pattern.CASE_INSENSITIVE);
        } else {
            this.m_pattern = Pattern.compile(restr);
        }
        this.m_matcharray = new ArrayList<Object>();
    }

    public ReExt(String restr) {
        this(restr, false);
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
        this.m_matcharray = new ArrayList<Object>();
        m = this.m_pattern.matcher(instr);
        while (m.find()) {
            List<String> cc = new ArrayList<String>();
            if (m.groupCount() == 1) {
                this.m_matcharray.add(m.group(1));
            } else if (m.groupCount() > 1) {
                for (i = 1; i <= m.groupCount(); i++) {
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

    public String getMatch(int i , int j) {
        String retstr = null;
        Object obj = null;
        ArrayList<String> aobj = new ArrayList<String>();
        if (i < this.m_matcharray.size()) {
            obj = this.m_matcharray.get(i);
            if (obj instanceof ArrayList) {
                aobj = ArrayList.class.cast(obj);
                if (aobj.size() > j) {
                    if (j <= 0) {
                        return aobj.get(0);
                    } else {
                    	return aobj.get(j);
                    }
                } else {
                	return null;
                }
            } else if (obj  instanceof String) {
            	if (j <= 0) {
            		return (String) obj;
            	}
            	return null;
            } 
        }
        return null;
    }

    public int  getCount() {
        return this.m_matcharray.size();
    }

    public int getCount(int i) {
        Object obj;
        ArrayList<String> aobj = new ArrayList<String>();
        if (i >= this.m_matcharray.size()) {
            return 0;
        } 

        obj = this.m_matcharray.get(i);
        if (obj instanceof ArrayList ) {
            aobj = ArrayList.class.cast(obj);
            return aobj.size();
        } 
        return 1;
    }
}