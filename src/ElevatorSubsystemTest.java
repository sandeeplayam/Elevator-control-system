import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.jupiter.api.Test;

class ElevatorSubsystemTest {

	private ElevatorSubsystem elevator;
	Scheduler sched = new Scheduler("name");

	public ElevatorSubsystemTest() {
		elevator = new ElevatorSubsystem("One", sched);
	}

	@Test
	public void testElevatorSubSystem() {

		assertFalse(elevator == null);
		assertEquals(elevator.getEleName(), "One");
		assertEquals(elevator.getScheduler(), sched);

	}

	@Test
	public void testTriggerMotor() {

		assertFalse(elevator.getMotorOn());
		elevator.triggerMotor();
		assertTrue(elevator.getMotorOn());
		elevator.triggerMotor();
		assertFalse(elevator.getMotorOn());

	}

	@Test
	public void testGetListOfButtons() {
		ArrayList<Boolean> listOfButtons = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the
																									// list of buttons
		Collections.fill(listOfButtons, Boolean.FALSE); // filling the list with false boolean values
		assertEquals(listOfButtons, elevator.getListOfButtons());
	}

	@Test
	public void testGetListOfLamps() {

		ArrayList<Boolean> listOfLamps = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the
																									// list of lamps
		Collections.fill(listOfLamps, Boolean.FALSE); // filling the list with false boolean values
		assertEquals(listOfLamps, elevator.getListOfLamps());

	}

	@Test
	public void testSetListOfButtons() {

		ArrayList<Boolean> listOfButtons = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the
																									// list of buttons
		Collections.fill(listOfButtons, Boolean.TRUE); // filling the list with true boolean values
		assertNotEquals(listOfButtons, elevator.getListOfButtons());
		elevator.setListOfButtons(listOfButtons);
		assertEquals(listOfButtons, elevator.getListOfButtons());

	}

	@Test
	public void testSetListOfLamps() {
		ArrayList<Boolean> listOfLamps = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the
																									// list of buttons
		Collections.fill(listOfLamps, Boolean.TRUE); // filling the list with true boolean values
		assertNotEquals(listOfLamps, elevator.getListOfLamps());
		elevator.setListOfLamps(listOfLamps);
		assertEquals(listOfLamps, elevator.getListOfLamps());
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

		elevator.setDirectionUp(true);
		assertTrue(elevator.getDirectionUp());
		elevator.setDirectionUp(false);
		assertFalse(elevator.getDirectionUp());
	
	}

	@Test
	public void testSetDirectionDown() {
		elevator.setDirectionDown(true);
		assertTrue(elevator.getDirectionDown());
		elevator.setDirectionDown(false);
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
		assertFalse(elevator.getDirectionDown());		}

	@Test
	public void testSetDoorClosed() {

	elevator.setDoorClosed(true);
	assertTrue(elevator.getDoorClosed());
	
	}


	@Test
	public void testDoorClose() {
		elevator.doorClose();
		assertTrue(elevator.getDoorClosed());
	
	}

	@Test
	public void testDoorOpen() {
		elevator.doorOpen();
		assertFalse(elevator.getDoorClosed());	}

	@Test
	public void testSetDestinationFloor() {

		elevator.setDestinationFloor(3);
		assertEquals(3,elevator.getDestinationFloor());
	
	}
	
	
	@Test
	public void testGetDestinationFloor() {
		elevator.setDestinationFloor(1);
		assertEquals(1,elevator.getDestinationFloor());	}

	

	@Test
	public void testUserDestination() {

	elevator.userDestination(2);
	//check destination floor
	assertEquals(2,elevator.getDestinationFloor());
	//check if floor button is on 
	assertTrue(elevator.getListOfButtons().get(2).booleanValue());
	//check if floor lamp is on 
	assertTrue(elevator.getListOfLamps().get(2).booleanValue());
	}

//	@Test
//	public void testReachedDestination() {
//		elevator.reachedDestination(2);
//		
//		//check if floor button is off 
//		assertFalse(elevator.getListOfButtons().get(2).booleanValue());
//		//check if floor lamp is off
//		assertFalse(elevator.getListOfLamps().get(2).booleanValue());
//	}

}
