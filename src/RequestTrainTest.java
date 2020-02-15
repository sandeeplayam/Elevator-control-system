import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class RequestTrainTest {

	private RequestTrain reqT;

	public RequestTrainTest() {

		reqT = new RequestTrain();

	}

	@Test
	public void testAdd() {

		Request req = new Request(LocalTime.now(), 1, "Up", 3);
		reqT.add(req);
		assertFalse(reqT.getAllRequests().isEmpty());

	}

	@Test
	public void testRemove() {

		Request req = new Request(LocalTime.now(), 1, "Up", 3);
		reqT.add(req);
		assertFalse(reqT.getAllRequests().isEmpty());
		reqT.remove(1);
		assertTrue(reqT.getAllRequests().isEmpty());

	}

	@Test
	public void testGetRequest() {

		Request req = new Request(LocalTime.now(), 1, "Up", 3);
		reqT.add(req);
		assertEquals(req, reqT.getRequest(1));

	}

	@Test
	public void testGetEarliest() {

		Request req1 = new Request(LocalTime.now(), 1, "Up", 3);
		Request req2 = new Request(LocalTime.MAX, 1, "Up", 3);
		reqT.add(req1);
		reqT.add(req2);

		assertEquals(req1, reqT.getEarliest());

	}

	@Test
	public void testGetByFloor() {

		HashMap<Integer, Request> rqsts = new HashMap<Integer, Request>();
		Request req1 = new Request(LocalTime.now(), 1, "Up", 7);
		Request req2 = new Request(LocalTime.MAX, 1, "Up", 4);
		Request req3 = new Request(LocalTime.MIN, 3, "Down", 1);
		Request req4 = new Request(LocalTime.now(), 4, "Up", 5);

		// key
		reqT.add(req1);// 1
		reqT.add(req2);// 2
		reqT.add(req3);// 3
		reqT.add(req4);// 4

		rqsts = reqT.getByFloor(4, true);

		// for req1
		assertEquals(req1, rqsts.get(1));

		// for req4
		assertEquals(req4, rqsts.get(4));

	}

}
