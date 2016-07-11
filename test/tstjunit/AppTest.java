import org.junit.Test;


public class AppTest {
	@Test
	public void testAssertTrue() {
		assertTrue("failure should be true",True);
	}

	@Test
	public void testAssertFail() {
		assertFail("failure should be false",False);
	}
}