/**
 * 
 */
package elevator_concurrency;

import org.junit.Test;

/**
 * @author danie
 *
 */
public class ElevatorSystemTest {

	@Test
	public void test() {
		Scheduler sh = new Scheduler("scheduler");
		sh.start();
	}

}
