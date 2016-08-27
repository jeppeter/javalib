package reext;

import reext.ReExt;
import static org.junit.Assert.assertEquals;
import org.junit.*;

public class ReExtTest {
	@Test
	public void BasicTest() {
		ReExt ext = new ReExt("([\\d]+)");
		assertEquals("match 3322",true,ext.Match("3322"));		
	}

	@Test
	public void GetCountTest() {
		ReExt ext = new ReExt("([\\d]+)cc([\\d]+)");
		ext.FindAll("33cc55");
		assertEquals("get 33cc55 with 2",1,ext.getCount());
		assertEquals("get [0] count",2,ext.getCount(0));
		assertEquals("get [0,0] 33","33",ext.getMatch(0,0));
		assertEquals("get [0,1] 55","55",ext.getMatch(0,1));
		assertEquals("get [1] null",null,ext.getMatch(1,-1));
		assertEquals("get [0,2] null",null,ext.getMatch(0,2));
	}

	@Test
	public void SplitTest() {
		String restr="\\s+";
		String instr ="hello world";
		String[] retstr = ReExt.Split(restr,instr);
		assertEquals(String.format("%s split %s with 2",instr,restr),2,retstr.length);
		assertEquals(String.format("%s split %s [0] hello",instr,restr),"hello",retstr[0]);
		assertEquals(String.format("%s split %s [0] world",instr,restr),"world",retstr[1]);
	}	

}