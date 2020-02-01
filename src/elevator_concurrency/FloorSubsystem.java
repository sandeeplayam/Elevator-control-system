package elevator_concurrency;

import java.util.ArrayList;
import java.util.List;

public class FloorSubsystem extends Thread {
	private Scheduler scheduler;
	private List<String> list;

	public FloorSubsystem(String name, List<String> list, Scheduler scheduler) {
		super(name);
		this.scheduler = scheduler;
		this.list = new ArrayList<>(list);
	}

	public void addToRequestList(String floor) {
		this.list.add(floor);
	}

	public void run() {
		for (int i = list.size(); i > 0; i--) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				System.err.println(e);

			}
			this.scheduler.scheduleRequest(list.remove(0));
		}
	}

}
