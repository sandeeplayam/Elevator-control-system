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
		// create request
		Request req = new Request(LocalTime.now(), 1, "Up", 3);
		// add request
		reqT.add(req);
		// check if map of requests is not empty
		assertFalse(reqT.getAllRequests().isEmpty());

	}

	@Test
	public void testRemove() {

		// create request
		Request req = new Request(LocalTime.now(), 1, "Up", 3);
		// add request
		reqT.add(req);
		// check if requests not empty
		assertFalse(reqT.getAllRequests().isEmpty());
		// remove request
		reqT.remove(1);
		// check if requests is empty
		assertTrue(reqT.getAllRequests().isEmpty());

	}

	@Test
	public void testGetRequest() {
		// create and add a request
		Request req = new Request(LocalTime.now(), 1, "Up", 3);
		reqT.add(req);
		// check if requests are the same
		assertEquals(req, reqT.getRequest(1));

	}

	@Test
	public void testGetEarliest() {

		// create and add requests
		Request req1 = new Request(LocalTime.now(), 1, "Up", 3);
		Request req2 = new Request(LocalTime.MAX, 1, "Up", 3);
		reqT.add(req1);
		reqT.add(req2);
		// check if earliest request is req1 (now is earlier than max)
		assertEquals(req1, reqT.getEarliest());

	}

	@Test
	public void testGetByFloor() {

		// create empty HashMap
		HashMap<Integer, Request> rqsts = new HashMap<Integer, Request>();
		// create requests
		Request req1 = new Request(LocalTime.now(), 1, "Up", 7);
		Request req2 = new Request(LocalTime.MAX, 1, "Up", 4);
		Request req3 = new Request(LocalTime.MIN, 3, "Down", 1);
		Request req4 = new Request(LocalTime.now(), 4, "Up", 5);

		// add requests to requestTrain HashMap
		// // Keys
		reqT.add(req1);// 1
		reqT.add(req2);// 2
		reqT.add(req3);// 3
		reqT.add(req4);// 4

		// add requests with destination above 4th floor to empty HashMap
		rqsts = reqT.getByFloor(4, true);

		// get req1 (using key 1) and check if it's the same
		assertEquals(req1, rqsts.get(1));

		// for req4
		assertEquals(req4, rqsts.get(4));

		// check that only 2 requests (req1 and req4) were returned
		assertEquals(rqsts.size(), 2);

	}

	@Test
	public void testToString() {
		
		String test = "Key: 1 Time: 00:00 Start: 1 Direction: UP Destination: 2\n";
		
		reqT.add(new Request(LocalTime.MIDNIGHT,1,"UP",2));
		assertEquals(test,reqT.toString());
		
	}
	
	
}
