/**
 * @author Group #2
 *
 */
public class EventErrorHandler {

	private String[] str;

	public EventErrorHandler(String[] str) {
		this.str = str;
	}

	public boolean strChecker() {
		if (this.checkTime() == false && this.checkFloor() == false && this.checkDirection() == false
				&& this.checkDestFloor() == false) {
			return true;
		}
		return false;
	}

	public boolean checkTime() {
		String time = this.str[0];
		if (time.length() != 10 || time.charAt(2) != ':' || time.charAt(5) != ':' || time.charAt(8) != '.') {
			System.out.println("The time is not in the correct format, cannot process request. \n");
			return true;
		}
		return false;
	}

	public boolean checkFloor() {
		if (!str[1].matches("-?\\d+")) {
			System.out.println("Did not receive a decimal for floor number, cannot process request. \n");
			return true;
		}
		return false;
	}

	public boolean checkDirection() {
		String dir = str[2];
		dir = dir.toUpperCase();
		if (dir.contains("UP") == false && dir.contains("DOWN") == false) {
			System.out.print("Did not receive a direction for the elevator to go, cannot process request. \n");
			return true;
		}
		return false;
	}

	public boolean checkDestFloor() {
		if (!str[3].matches("-?\\d+")) {
			System.out.println("Did not receive a decimal for destination floor, cannot process request. \n");
			return true;
		}
		return false;
	}

}
