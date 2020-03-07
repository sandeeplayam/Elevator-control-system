import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SchedulerUdp extends Thread {
	private DatagramSocket sendSocket, receiveSocket;
	private DatagramPacket sendPacket, receivePacket;
	private final InetAddress ElevatorInetAdress;

	public SchedulerUdp(String ip) throws UnknownHostException {
		this.ElevatorInetAdress = InetAddress.getByName(ip);
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
			// Process the received datagram.
			System.out.print("Packet received contaning:\t");
			int len = receivePacket.getLength();

			// Form a String from the byte array.
			String received = new String(data, 0, len);
			System.out.println(received);

			// We're finished, so close the sockets.
			receiveSocket.close();

			data[data.length - 2] = 1;
			// send the received packet to elevator
			this.sendRequestToElevator(data);
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

	public void sendRequestToElevator(byte[] data) {

		try {
			// initialize socket and packet to send to elevator
			this.sendSocket = new DatagramSocket();
			sendPacket = new DatagramPacket(data, receivePacket.getLength(), ElevatorInetAdress, 3001);
			System.out.println("Scheduler: Sending packet to elevator:");

			// Slow things down (wait 2 seconds)
			Thread.sleep(2000);

			// Send the datagram packet to the client via the send socket.
			sendSocket.send(sendPacket);

			System.out.println("Schuduler: sent to Elevator");

			// We're finished, so close the sockets.
			sendSocket.close();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		this.receiveFloorRequest();
	}

	public static void main(String[] args) throws UnknownHostException {

		SchedulerUdp sh = new SchedulerUdp("172.17.155.10");
		sh.start();

	}

}
