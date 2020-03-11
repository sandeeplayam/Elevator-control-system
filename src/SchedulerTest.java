import static org.junit.jupiter.api.Assertions.*;

import java.net.UnknownHostException;
import java.time.LocalTime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

class SchedulerTest {

	private Scheduler sched;

	public SchedulerTest() throws UnknownHostException {

		sched = new Scheduler("name");
	}

	@Test
	public void testScheduler() {

		assertEquals(sched.getName(), "name");

	}

/*
	@Test
	public void testgetCurrentState() {
		
		//sched.setCurrentState(RequestState.SLEEP);

		assertEquals(SchedulerStates.SCHEDULER, sched.getCurrentState());
	}

	@Test
	public void testsetCurrentState() {

		sched.setCurrentState(SchedulerStates.SLEEP);
		assertEquals(SchedulerStates.SLEEP, sched.getCurrentState());

	}

	@Test
	public void testaddEleveterRequest() {

		Request req = new Request(LocalTime.now(), 1, "Up", 3);

		sched.addEleveterRequest(1, req);

		assertEquals(req, sched.getCurrentRequest().getValue());
		assertEquals(1, sched.getCurrentRequest().getKey());

	}

	@Test
	public void testremoveEleveterRequest() {

		Request req = new Request(LocalTime.now(), 1, "Up", 3);

		sched.addEleveterRequest(1, req);
		assertFalse(sched.isElevatorEmpty());
		sched.removeEleveterRequest(1, req);
		assertTrue(sched.isElevatorEmpty());

	}

	@Test
	public void testsetCurrentRequest() {
		Request req = new Request(LocalTime.now(), 1, "Up", 3);

		sched.setCurrentRequest(1, req);

		assertEquals(sched.getCurrentRequest().getValue(), req);
		assertEquals(sched.getCurrentRequest().getKey(), 1);
	}
*/
}
