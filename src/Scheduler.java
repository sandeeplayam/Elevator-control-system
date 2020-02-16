
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

	// floor inserts a requests
	synchronized public void scheduleRequest(Integer key, Request request) {
		while (!currentState.compare(RequestState.FLOOR_REQUEST)) {
			try {
				wait();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		String fThread = Thread.currentThread().getName();
		int f = request.getDestFloor();
		System.out.println(fThread + " sending a request to floor " + f);
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			System.err.println(e);

		}
		requestListMap.put(key, request);
		System.out.println("Scheduler recieved a request to : " + request.getDestFloor());
		this.addEleveterRequest(key, request);
		currentState = RequestState.ELEVATOR_REQUEST;
		notifyAll();
	}

	/**
	 * 
	 * @param request
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

	public void setCurrentState(RequestState r) {
		this.currentState = r;
	}

	public void addEleveterRequest(Integer key, Request request) {
		elevtorListMap.put(key, request);
		this.setCurrentRequest(key, request);
	}

	public void removeEleveterRequest(Integer key, Request request) {
		elevtorListMap.remove(key);
	}

	public void setCurrentRequest(Integer currentKey, Request currentRequest) {
		this.currentRequest = currentRequest;
		this.currentKey = currentKey;
	}

	public Map.Entry<Integer, Request> getCurrentRequest() {
		return new AbstractMap.SimpleEntry<Integer, Request>(currentKey, currentRequest);
	}

	public boolean isElevatorEmpty() {
		return elevtorListMap.isEmpty();
	}

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
