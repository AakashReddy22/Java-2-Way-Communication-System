package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server.ClientInfo;

public class Client {

	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	private boolean running;
	
	public Client(String name, String address, int port) {
		try {
			this.address = InetAddress.getByName(address);
			this.port = port;
			
			socket = new DatagramSocket();
			running = true;
			listen();
			send("\\conn:"+name);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void send(String message) {
		try {
			// Creating the end identifier into "message"
			message += "\\e";
			// converting message string into a byte array
			byte[] data = message.getBytes();
			// This is the organization of the message that were are sending
			DatagramPacket packet = new DatagramPacket(data, data.length,address, port);
			socket.send(packet);
			System.out.println("Sent message to, "+address.getHostAddress()+":"+port);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void listen() {
		Thread listenThread = new Thread("ChatProgram Listener"){
			// When the thread is started, this run method gets initiated
			public void run() {
				try {
					// While the server is running we are going to create a byte array and a DatagramPacket that writes to our data variable
					while (running) {
						
						// Byte array to get our data
						byte[] data =  new byte[1024];
						// To get the data we do this
						DatagramPacket packet = new DatagramPacket(data, data.length);
						// Getting the data into the packet to send to the array
						// Were using a packet instead of direct transfer between the socket and the array, because we get access to more information,which allows the server to keep running for as long as possible
						//This is placed after the thread and in the try catch as the program freezes after the listen method in line 17
						socket.receive(packet);
						// careful
						String message = new String(data);
						// Identifier at the end of the message, which is \e to tell the server where the end of our message is so if we want to alter that message,we dont have to search the whole array's 1024 characters 
						message = message.substring(0, message.indexOf("\\e"));
						
						//MANAGE MESSAGE
						if (!isCommand(message, packet)) {
							ClientWindow.printToConsole(message);
						}
					}
						
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			// starting the thread
		}; listenThread.start();
		
	}
	
private static boolean isCommand (String message, DatagramPacket packet) {
		
		// Array list is adaptive to the program running. The array list will resize the array automatically when the program is running
		if (message.startsWith("\\con:")) {
			// RUN connection code
			
			return true;
		}
		
		
		return false;
	}
	

}
