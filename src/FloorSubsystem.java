
/**
 * @author Group #2
 *
 */
import java.util.ArrayList;
import java.util.List;

public class FloorSubsystem extends Thread {
	private Scheduler scheduler;
	private List<Request> requestList;

	public FloorSubsystem(String name, List<Request> list, Scheduler scheduler) {
		super(name);
		this.scheduler = scheduler;
		this.requestList = new ArrayList<>(list);
	}

	public void addToRequestList(Request floor) {
		this.requestList.add(floor);
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
				this.scheduler.scheduleRequest(requestList.remove(0));
			}
		}
	}

}
