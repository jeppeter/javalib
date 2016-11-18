package com.github.jeppeter.test;

import com.github.jeppeter.reext.ReExt;

public class ReTest {
    public static void main(String[] args) {
        String restr = "\\s+";
        String instr = "hello world";
        String[] retstr = ReExt.Split(restr, instr);
        int i;
        for (i = 0; i < retstr.length; i++) {
            System.out.println(String.format("[%d]=%s", i, retstr[i]));
        }
        instr = "cc ss bb";
        retstr = ReExt.Split(restr, instr, 2);
        System.out.println("split max limit");
        for (i = 0; i < retstr.length; i++) {
            System.out.println(String.format("[%d]=%s", i, retstr[i]));
        }
        return;
    }
}
