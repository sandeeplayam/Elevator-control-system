import java.util.*;
//import java.sql.Time;
//import java.time.LocalTime;

public class RequestTrain {

	private HashMap<Integer, Request> requests; // list of request objects

	public RequestTrain() {

		this.requests = new HashMap<Integer, Request>();
	}

	//add a request
	public void add(Request req) {

		int key = 0;
		if (!requests.isEmpty()) {
			key += requests.size();
		}
		requests.put(key, req);

	}

	//remove a request given a key
	public void remove(int key) {

		requests.remove(key);

	}

	//get a request given a key
	public Request getRequest(int key) {

		return requests.get(key);

	}

	//get request with earliest time
	public Request getEarliest() {

		Request earliest = null;
	//	LocalTime check = LocalTime.MAX;  // initialize as max time

		for (Request req : requests.values()) {
			
			
			if (earliest == null) {
				//set first request as earliest
				earliest = req;
			} else if (earliest.getTime().isAfter(req.getTime())) {

				earliest = req;
			}

		}

		return earliest;

	}

	//return requests that are above or below a given floor number
	// floor is starting position and up is boolean for direction
	public HashMap<Integer, Request> getByFloor(int floor, boolean up) {

		HashMap<Integer, Request> rqsts = new HashMap<Integer, Request>();

		String dir = "";

		if (up) {
			dir = "Up";
		} else {
			dir = "Down";
		}

		Iterator<Map.Entry<Integer, Request>> it = requests.entrySet().iterator();

		while (it.hasNext()) {

			Map.Entry<Integer, Request> req = it.next();

			if (req.getValue().getDirection().equals(dir)) {

				if (up && req.getValue().getDestFloor() > floor) {

					rqsts.put(req.getKey(), req.getValue());

				} else if (!up && req.getValue().getDestFloor() < floor) {

					rqsts.put(req.getKey(), req.getValue());

				}
			}

			it.remove(); // avoids a ConcurrentModificationException
		}

		return rqsts;

	}
}
