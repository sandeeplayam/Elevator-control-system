import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.jupiter.api.Test;

class ElevatorSubsystemTest {

	// Initialize elevator and scheduler
	private ElevatorSubsystem elevator;
	Scheduler sched = new Scheduler("name");

	public ElevatorSubsystemTest() {
		// create a new elevator
		elevator = new ElevatorSubsystem("One", sched);
	}

	@Test
	public void testElevatorSubSystem() {

		// Check if..
		// elevator exists
		assertFalse(elevator == null);
		// name is "One"
		assertEquals(elevator.getEleName(), "One");
		// elevator has same scheduler
		assertEquals(elevator.getScheduler(), sched);
		// initial floor is 1
		assertEquals(elevator.getCurrFloor(), 1);
		// door is initially closed
		assertTrue(elevator.getDoorClosed());
		// list of buttons and lamps exist (not empty)
		assertFalse(elevator.getListOfButtons().isEmpty());
		assertFalse(elevator.getListOfLamps().isEmpty());
	}

	@Test
	public void testTriggerMotor() {

		// check if motor is initially off
		assertFalse(elevator.getMotorOn());
		// turn on
		elevator.triggerMotor();
		// check if on
		assertTrue(elevator.getMotorOn());
		// turn off
		elevator.triggerMotor();
		// check if off
		assertFalse(elevator.getMotorOn());

	}

	@Test
	public void testGetListOfButtons() {
		ArrayList<Boolean> listOfButtons = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the
																									// list of buttons
		Collections.fill(listOfButtons, Boolean.FALSE); // filling the list with false boolean values
		assertEquals(listOfButtons, elevator.getListOfButtons());// check if equal
	}

	@Test
	public void testGetListOfLamps() {

		ArrayList<Boolean> listOfLamps = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the
																									// list of lamps
		Collections.fill(listOfLamps, Boolean.FALSE); // filling the list with false boolean values
		assertEquals(listOfLamps, elevator.getListOfLamps());// check if equal

	}

	@Test
	public void testSetListOfButtons() {

		ArrayList<Boolean> listOfButtons = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the
																									// list of buttons
		Collections.fill(listOfButtons, Boolean.TRUE); // filling the list with true boolean values
		assertNotEquals(listOfButtons, elevator.getListOfButtons()); // check if they aren't equal (elevator should have
																		// false values)
		elevator.setListOfButtons(listOfButtons);// set elevator values to true
		assertEquals(listOfButtons, elevator.getListOfButtons());// check if equal

	}

	@Test
	public void testSetListOfLamps() {
		ArrayList<Boolean> listOfLamps = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the
																									// list of buttons
		Collections.fill(listOfLamps, Boolean.TRUE); // filling the list with true boolean values
		assertNotEquals(listOfLamps, elevator.getListOfLamps());// check if they aren't equal (elevator should have
																// false values)
		elevator.setListOfLamps(listOfLamps);// set elevator values to true
		assertEquals(listOfLamps, elevator.getListOfLamps());// check if equal
	}

	@Test
	public void testGetEleName() {

		assertEquals("One", elevator.getEleName());
	}

	@Test
	public void testGetCurrFloor() {
		assertEquals(1, elevator.getCurrFloor());
	}

	@Test
	public void testSetDirectionUp() {

		// set direction to true
		elevator.setDirectionUp(true);
		// check if true
		assertTrue(elevator.getDirectionUp());
		// set to false
		elevator.setDirectionUp(false);
		// check if false
		assertFalse(elevator.getDirectionUp());

	}

	@Test
	public void testSetDirectionDown() {
		// set direction to true
		elevator.setDirectionDown(true);
		// check if true
		assertTrue(elevator.getDirectionDown());
		// set direction to false
		elevator.setDirectionDown(false);

		// check if false
		assertFalse(elevator.getDirectionDown());
	}

	@Test
	public void testGetDirectionUp() {
		elevator.setDirectionUp(true);
		assertTrue(elevator.getDirectionUp());
		elevator.setDirectionUp(false);
		assertFalse(elevator.getDirectionUp());
	}

	@Test
	public void testGetDirectionDown() {
		elevator.setDirectionDown(true);
		assertTrue(elevator.getDirectionDown());
		elevator.setDirectionDown(false);
		assertFalse(elevator.getDirectionDown());
	}

	@Test
	public void testSetDoorClosed() {
		// set to true
		elevator.setDoorClosed(true);
		// check if true
		assertTrue(elevator.getDoorClosed());

	}


	@Test
	public void testDoorClose() {
		// close door
		elevator.doorClose();
		// check if door closed
		assertTrue(elevator.getDoorClosed());

	}

	@Test
	public void testDoorOpen() {
		// open door
		elevator.doorOpen();

		// check if door open

		assertFalse(elevator.getDoorClosed());
	}

	@Test
	public void testSetDestinationFloor() {
		// set destination to 3
		elevator.setDestinationFloor(3);
		// check if floor is 3
		assertEquals(3, elevator.getDestinationFloor());

	}

	@Test
	public void testGetDestinationFloor() {
		elevator.setDestinationFloor(1);
		assertEquals(1, elevator.getDestinationFloor());
	}

	@Test
	public void testUserDestination() {


		// toggle user destination at 2
		elevator.userDestination(2);
		// check destination floor is 2
		assertEquals(2, elevator.getDestinationFloor());
		// check if floor button 2 is on
		assertTrue(elevator.getListOfButtons().get(2).booleanValue());
		// check if floor lamp 2 is on
		assertTrue(elevator.getListOfLamps().get(2).booleanValue());
	}

	@Test
	public void testReachedDestination() {

		// toggle user destination at 2
		elevator.userDestination(2);
		// toggle reached destination at 2
		elevator.reachedDestination(2);

		// check if floor button 2 is off
		assertFalse(elevator.getListOfButtons().get(2).booleanValue());
		// check if floor lamp 2 is off
		assertFalse(elevator.getListOfLamps().get(2).booleanValue());
		// check if door closed
		assertTrue(elevator.getDoorClosed());

	}

}
