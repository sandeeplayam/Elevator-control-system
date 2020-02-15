import static org.junit.Assert.*;

import java.time.LocalTime;

import org.junit.Test;
//YusufJ
public class requestTest {
private Request Request;

	@Test
	public void testgetStartFloor() {
		Request r = new Request(LocalTime.NOON,2,"UP",4);
		assertEquals(2, r.getStartFloor());
		//assertFalse(3, r.getStartFloor());
		
	}
	
	@Test
	public void testgetTime() {
		Request r1 = new Request(LocalTime.MIDNIGHT, 3,"down",5);
		assertEquals(LocalTime.MIDNIGHT, r1.getTime());
		
	}
	@Test
	public void testgetDirection() {
		Request r5 = new Request(LocalTime.now(), 3,"down",2);
		assertEquals("down", r5.getDirection());
		String direction =  "down";
		assertTrue(r5.getDirection().equals(direction));
		
		}
		
	
	@Test
	public void testgetDestFloor() {
		Request r3 = new Request(LocalTime.MIDNIGHT, 3,"down",2);
		assertFalse(r3.getDestFloor()==6);//should assert false since destination actual is 5
		
	}
	/**
	@Test
	public void testtoString() {
		Request r4 = new Request(LocalTime.MIN, 3,"up",5);
		String result = " 4:10 3 up 5";
	//	assertEquals(r4.toString(), result);
		assertTrue(r4.toString().contentEquals(result));
		
	}
*/
}
