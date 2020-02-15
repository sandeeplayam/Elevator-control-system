
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

	public void run() {
		for (;;) {
			// check if there is more requests coming in
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				System.err.println(e);

			}
			if (isRequest()) {
				this.scheduler.setCurrentState(RequestState.SLEEP);
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
