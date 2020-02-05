
/**
 * @author Group #2
 *
 */
import java.util.ArrayList;
import java.util.List;

public class Scheduler extends Thread {
	private RequestState currentState = RequestState.SCHEDULER;
	private List<Request> requestList = new ArrayList<Request>();

	public Scheduler(String name) {
		super(name);
	}

	// floor inserts a requests
	synchronized public void scheduleRequest(Request request) {
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			System.err.println(e);

		}
		requestList.add(request);
		while (!currentState.compare(RequestState.FLOOR_REQUEST)) {
			try {
				wait();
			} catch (Exception e) {
				System.err.println(e);
			}
		}

		System.out.println(Thread.currentThread().getName() + " triggered a request : " + request);
		currentState = RequestState.ELEVATOR_REQUEST;
		notifyAll();
	}

	/**
	 * 
	 * @param request
	 */
	synchronized public void excuteRequest(String request) {
		while (!currentState.compare(RequestState.ELEVATOR_REQUEST)) {
			try {
				wait();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		System.out.println(Thread.currentThread().getName() + ": " + request);
		requestList.remove(0);
		currentState = RequestState.FLOOR_REQUEST;
		notifyAll();
	}

	synchronized public void setCurrentState(RequestState r) {
		this.currentState = r;
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
