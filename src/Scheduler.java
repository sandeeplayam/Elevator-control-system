
/**
 * @author Group #2
 *
 */
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Scheduler extends Thread {
	private RequestState currentState = RequestState.SCHEDULER;
	private HashMap<Integer, Request> requestListMap;
	private HashMap<Integer, Request> elevtorListMap;
	private int currentKey;
	private Request currentRequest;

	public Scheduler(String name) {
		super(name);
		requestListMap = new HashMap<Integer, Request>();
		elevtorListMap = new HashMap<Integer, Request>();
	}

	/**
	 * Schedule a request
	 * 
	 * @param key     , id or order of the request
	 * @param request , the request to be processed
	 */

	synchronized public void scheduleRequest(Integer key, Request request) {
		while (!currentState.compare(RequestState.FLOOR_REQUEST)) {
			try {
				wait();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		String fThread = Thread.currentThread().getName();
		System.out.println(fThread + " sending a request with details of---> " + request);
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			System.err.println(e);

		}
		requestListMap.put(key, request);
		int f = request.getDestFloor();
		System.out.println("Scheduler recieved a request to : " + f);
		this.addEleveterRequest(key, request);
		currentState = RequestState.ELEVATOR_REQUEST;
		notifyAll();
	}

	/**
	 * 
	 * @return an entry of the request handled
	 */
	synchronized public Map.Entry<Integer, Request> executeRequest() {
		while (!currentState.compare(RequestState.ELEVATOR_REQUEST)) {
			try {
				wait();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		currentState = RequestState.ElevatorRunning;
		notifyAll();
		requestListMap.remove(currentKey);
		return new AbstractMap.SimpleEntry<Integer, Request>(currentKey, currentRequest);
	}

	/**
	 * 
	 * @return run the completed request / notified
	 */
	synchronized public void runCompleted() {
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			System.err.println(e);

		}
		this.setCurrentState(RequestState.FLOOR_REQUEST);
		removeEleveterRequest(currentKey, currentRequest);
		notifyAll();
	}

	/**
	 * change the current state to r
	 * 
	 * @param r
	 */
	public void setCurrentState(RequestState r) {
		this.currentState = r;
	}

	/**
	 * add request to the elevator queue
	 * 
	 * @param key,     id or order of the request to elevator
	 * @param request, request to be added to elevator
	 */
	public void addEleveterRequest(Integer key, Request request) {
		elevtorListMap.put(key, request);
		this.setCurrentRequest(key, request);
	}

	/**
	 * remove request from the elevator queue
	 * 
	 * @param key,     id or order of the request to elevator
	 * @param request, request to be removed from elevator queue
	 */

	public void removeEleveterRequest(Integer key, Request request) {
		elevtorListMap.remove(key);
	}

	/**
	 * set current request to be handled
	 * 
	 * @param currentKey
	 * @param currentRequest
	 */
	public void setCurrentRequest(Integer currentKey, Request currentRequest) {
		this.currentRequest = currentRequest;
		this.currentKey = currentKey;
	}

	/**
	 * 
	 * @return an entry of the current request
	 */
	public Map.Entry<Integer, Request> getCurrentRequest() {
		return new AbstractMap.SimpleEntry<Integer, Request>(currentKey, currentRequest);
	}

	/**
	 * 
	 * @return boolean, true if list empty
	 */
	public boolean isElevatorEmpty() {
		return elevtorListMap.isEmpty();
	}

	/**
	 * place the system in waiting mode
	 */
	synchronized public void waitForRequest() {
		while (this.isElevatorEmpty()) {
			try {
				System.out.println("*********waiting for request...*********");
				this.setCurrentState(RequestState.SLEEP);
				wait();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	/**
	 * start the thread
	 */
	synchronized public void run() {
		for (;;) {
			while (!currentState.compare(RequestState.SCHEDULER)) {
				try {
					wait();
				} catch (Exception e) {
					System.err.println(e);
				}
			}
			currentState = RequestState.FLOOR_REQUEST;
			notifyAll();
		}
	}
}
