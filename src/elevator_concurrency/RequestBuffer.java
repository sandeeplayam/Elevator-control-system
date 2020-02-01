package elevator_concurrency;

public class RequestBuffer {

	private boolean nextRequest = false;
//	private List<String> requestList = new ArrayList<String>();
//	private RequestState currentState = RequestState.REQUEST_WAIT;

	public RequestBuffer() {
	}

	// floor inserts a requests
	synchronized public void scheduleRequest(String request) { // HashMap<String, String>
		while (nextRequest) {
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		System.out.println(request);
		nextRequest = true;
		notifyAll();
	}

	// Elevator answers a requests
	synchronized public void excuteRequest(String request) { // HashMap<String, String> request
		while (!nextRequest) {
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		System.out.println(request);
		nextRequest = false;
		notifyAll();
	}

}
