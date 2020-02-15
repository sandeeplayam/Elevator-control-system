
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
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			System.err.println(e);

		}
		requestListMap.put(key, request);
		while (!currentState.compare(RequestState.FLOOR_REQUEST)) {
			try {
				wait();
			} catch (Exception e) {
				System.err.println(e);
			}
		}

		System.out.println(Thread.currentThread().getName() + " triggered a request : " + request);
		this.addEleveterRequest(key, request);
		currentState = RequestState.ELEVATOR_REQUEST;
		notifyAll();
	}

	/**
	 * 
	 * @param request
	 */
	synchronized public Map.Entry<Integer, Request> excuteRequest() {
		while (!currentState.compare(RequestState.ELEVATOR_REQUEST)) {
			try {
				wait();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		requestListMap.remove(currentKey);
		currentState = RequestState.FLOOR_REQUEST;
		notifyAll();
		return new AbstractMap.SimpleEntry<Integer, Request>(currentKey, currentRequest);
	}

	synchronized public void setCurrentState(RequestState r) {
		this.currentState = r;
	}

	synchronized public void addEleveterRequest(Integer key, Request request) {
		elevtorListMap.put(key, request);
		this.setCurrentRequest(key, request);
	}

	synchronized public void removeEleveterRequest(Integer key, Request request) {
		elevtorListMap.remove(key);
//		System.out.print((new HashMap<Integer, Request>()).put(key, request));
	}

	synchronized public void setCurrentRequest(Integer currentKey, Request currentRequest) {
		this.currentRequest = currentRequest;
		this.currentKey = currentKey;
	}

	synchronized public Map.Entry<Integer, Request> getCurrentRequest() {
		return new AbstractMap.SimpleEntry<Integer, Request>(currentKey, currentRequest);
	}

	synchronized public void waitForRequest() {
		for (;;) {
			while (currentState.compare(RequestState.SLEEP)) {
				try {
					System.out.println("*********waiting for request...*********");
					wait();
				} catch (Exception e) {
					System.err.println(e);
				}
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
