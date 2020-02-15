import java.util.*;
//import java.sql.Time;
//import java.time.LocalTime;

public class RequestTrain {

	private HashMap<Integer, Request> requests; // list of request objects

	/**Constructor for the RequestTrain object which holds a map of requests
	 * Creates a new HashMap
	 */
	public RequestTrain() {

		this.requests = new HashMap<Integer, Request>();
	}

	
	public HashMap<Integer, Request> getAllRequests(){
		return this.requests;
	}
	
	/**Adds a request to existing map of requests
	 * 
	 * @param req is the Request to be added
	 */
	public void add(Request req) {

		//initialize key (request ID) as 1
		int key = 1;
		//if there are other requests in map, add count to 1 to get new ID 
		//else add key as 1 since it's the first request to be added
		if (!requests.isEmpty()) {
			key += requests.size();
		}
		requests.put(key, req);

	}

	/**Remove a request from the map given the unique key of the request
	 * 
	 * @param key is the ID of the request to be removed
	 */
	public void remove(int key) {

		requests.remove(key);

	}

	/**Get a request given the unique key of the request
	 * 
	 * @param key is the ID of the request to be retrieved
	 * @return returns the Request
	 */
	public Request getRequest(int key) {

		return requests.get(key);

	}

	/**Gets the request with the earliest time stamp
	 * 
	 * @return returns the earliest Request
	 */
	public Request getEarliest() {

		Request earliest = null;
	//	LocalTime check = LocalTime.MAX;  // initialize as max time

		//check all the requests available
		for (Request req : requests.values()) {
			
			//for the first request
			if (earliest == null) {
				//set earliest to  first request 
				earliest = req;
			//if earliest's time is after current request
			} else if (earliest.getTime().isAfter(req.getTime())) {
				//set earliest to current request
				earliest = req;
			}

		}

		return earliest;

	}

	/**Gets a map of requests that have destinations that are above/below a given floor number
	 * 
	 * @param floor is the starting position 
	 * @param up is a boolean representing direction, true if checking requests above given floor 
	 * and false for down
	 * @return returns the HashMap of requests
	 */
	public HashMap<Integer, Request> getByFloor(int floor, boolean up) {

		//Initialize HashMap and direction string
		HashMap<Integer, Request> rqsts = new HashMap<Integer, Request>();
		String dir = "";

		//set string according to direction
		if (up) {
			dir = "Up";
		} else {
			dir = "Down";
		}

		Iterator<Map.Entry<Integer, Request>> it = requests.entrySet().iterator();

		//while there are requests
		while (it.hasNext()) {

			Map.Entry<Integer, Request> req = it.next();

			//if request has the needed direction
			if (req.getValue().getDirection().equals(dir)) {

				//if request is going up and above given floor
				if (up && req.getValue().getDestFloor() > floor) {
					//put in new HashMap
					rqsts.put(req.getKey(), req.getValue());
					
				//if request is going down and below given floor
				} else if (!up && req.getValue().getDestFloor() < floor) {
					
					//put in new HashMap
					rqsts.put(req.getKey(), req.getValue());

				}
			}

			it.remove(); // avoids a ConcurrentModificationException
		}

		return rqsts;

	}
}
