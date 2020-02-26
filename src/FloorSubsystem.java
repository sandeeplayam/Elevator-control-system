
/**
 * @author Group #2
 *
 */
import java.util.HashMap;
import java.util.Map;

public class FloorSubsystem extends Thread {
	private Scheduler scheduler;
	private HashMap<Integer, Request> requestList;

	FloorSubsystem(String name, HashMap<Integer, Request> list, Scheduler scheduler) {
		super(name);
		this.scheduler = scheduler;
		this.requestList = new HashMap<Integer, Request>(list);
	}

	public boolean isRequest() {
		return requestList.isEmpty();
	}

	synchronized public void run() {

		for (;;) {
			// check if there is more requests coming in
			if (isRequest()) {
				this.scheduler.waitForRequest();
			} else {
				for (Map.Entry<Integer, Request> entry : requestList.entrySet()) {
					this.scheduler.scheduleRequest(entry.getKey(), entry.getValue());
				}
				requestList.clear();
			}
		}
	}

}
