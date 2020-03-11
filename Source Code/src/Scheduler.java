import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Scheduler extends Thread {
	private DatagramSocket sendSocket, receiveSocket;
	private DatagramPacket sendPacket, receivePacket;
	private final InetAddress ElevatorInetAdress;
	private InetAddress FloorInetAdress;
	
	
	private final int numElevators = 3;
	/*
	private SchedulerStates currentState = SchedulerStates.SCHEDULER;
	private HashMap<Integer, Request> requestListMap;
	private HashMap<Integer, Request> elevtorListMap;
	private int currentKey;
	private Request currentRequest;
	*/
	private ArrayList<Integer> currFloor; //contains current floor of elevators 
	
	
	public Scheduler(String ip) throws UnknownHostException {
		this.ElevatorInetAdress = InetAddress.getByName(ip);
		
	//	requestListMap = new HashMap<Integer, Request>();
		//elevtorListMap = new HashMap<Integer, Request>();
		currFloor = new ArrayList<Integer>();
		currFloor.add(0);
		currFloor.add(0);
		currFloor.add(0);

		
	}
	
	public void processRequest(byte[] req, String Streq) {
		
		
		String[] splitReq = Streq.split(" ");
		//num of floors between floors
		int floorDiff=999;
		//elevator num to send req
		int choice =0;
		
		for(int i =0;i<numElevators;i++) {
			
			
			
			
			if(Math.abs(Integer.parseInt(splitReq[1]) - currFloor.get(i))<floorDiff) {
				
				floorDiff = Math.abs(Integer.parseInt(splitReq[1]) - currFloor.get(i));
				choice = i+1;
				
			}
			
		}
				
		this.sendRequestToElevator(req, choice);
		
		this.receiveFloorNum();
	}
	
	public void receiveFloorNum() {
		
		
		try {

			// Initialize socket and listen to port 2000
			this.receiveSocket = new DatagramSocket(2000);

			// Construct a DatagramPacket for receiving packets up
			// to 100 bytes long
			byte data[] = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Scheduler: Checking current elevator locations..");

			receiveSocket.receive(receivePacket);
			
			// Process the received datagram.
			System.out.println("Elevator locations up to date:\n");
			int len = receivePacket.getLength();

			// Form a String from the byte array.
			String received = new String(data, 0, len);
			
			String[] splitStr = received.split(" ");
			
			//if not first 3 echoes
			if(!splitStr[0].equals("x")) {
				
				this.sendRequestToFloor(data);

			}
			
			//set current floor of elevator
			this.setCurrFloor(Integer.parseInt(splitStr[5]), Integer.parseInt(splitStr[6]));
			
			// We're finished, so close the sockets.
			receiveSocket.close();
			

		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}	
	}



	
	public void sendRequestToElevator(byte[] data, int ele) {

		try {
			// initialize socket and packet to send to elevator
			this.sendSocket = new DatagramSocket();
			sendPacket = new DatagramPacket(data, receivePacket.getLength(), ElevatorInetAdress, (2000+ele));
			System.out.println("Scheduler: Sending packet to elevator:");

			// Slow things down (wait 2 seconds)
			Thread.sleep(2000);

			// Send the datagram packet to the client via the send socket.
			sendSocket.send(sendPacket);

			System.out.println("Scheduler: sent to Elevator");

			// We're finished, so close the sockets.
			sendSocket.close();
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	public void receiveRequestFromElevator() {

		try {

			// Initialize socket and listen to port 5000
			this.receiveSocket = new DatagramSocket(5000);

			// Construct a DatagramPacket for receiving packets up
			// to 100 bytes long
			byte data[] = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Scheduler: Waiting for response..");

			receiveSocket.receive(receivePacket);

			Thread.sleep(1000);
			// Process the received datagram.
			System.out.print("Packet received contaning:\t");
			int len = receivePacket.getLength();

			// Form a String from the byte array.
			String received = new String(data, 0, len);
			System.out.println(received);

			// We're finished, so close the sockets.
			receiveSocket.close();

			
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * Initialize socket and bind it to port 3000
	 */
	public void receiveFloorRequest() {

		try {
			

			// Initialize socket and listen to port 3000
			this.receiveSocket = new DatagramSocket(3000);

			// Construct a DatagramPacket for receiving packets up
			// to 100 bytes long
			byte data[] = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Scheduler: Waiting for request..");

			receiveSocket.receive(receivePacket);
			
			
				Thread.sleep(1000);
			
			
			// Process the received datagram.
			System.out.print("Packet received contaning:\t");
			int len = receivePacket.getLength();

			// Form a String from the byte array.
			String received = new String(data, 0, len);
			System.out.println(received);

			// We're finished, so close the sockets.
			receiveSocket.close();
			
			
			FloorInetAdress = receivePacket.getAddress();
			
			
			this.processRequest(data, received);
			
			// send the received packet to elevator
			//this.sendRequestToElevator(data);
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void sendRequestToFloor(byte[] data) {

		try {
			// initialize socket and packet to send to elevator
			this.sendSocket = new DatagramSocket();
			sendPacket = new DatagramPacket(data, receivePacket.getLength(), FloorInetAdress, 3003);
			System.out.println("Scheduler: Sending packet to elevator:");

			// Slow things down (wait 2 seconds)
			Thread.sleep(2000);

			// Send the datagram packet to the client via the send socket.
			sendSocket.send(sendPacket);

			System.out.println("Schuduler: sent to Elevator");

			// We're finished, so close the sockets.
			sendSocket.close();
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//sets current floor of elevator #ElevatNum 
	public void setCurrFloor(int ElevatNum, int floorNum) {
		
		currFloor.add(ElevatNum-1, floorNum); 
		
	}

	public void run() {
		
		for(int i = 0; i<3; i++) {
		
			this.receiveFloorNum();
			
		}
		
		for(;;) {
			
			this.receiveFloorRequest();

		}
		
	}

	public static void main(String[] args) throws UnknownHostException {

		Scheduler sh = new Scheduler("172.17.52.157");
		sh.start();

	}

}