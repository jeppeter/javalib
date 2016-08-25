package reext;

import ReExt;

public class ReExtTest {
	@Test
	public void FirstTest() {
		ReExt ext = new ReExt("([\d]+)");
		assertEquals("match 3322",true,ext.Match("3322"));		
	}
}