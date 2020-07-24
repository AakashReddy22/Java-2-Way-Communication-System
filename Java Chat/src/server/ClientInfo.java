package server;

import java.net.InetAddress;

// This class essentially stores the client information into an array
// The array will be used for sending messages and client history
// Will be storing the clinet's ping and the last time the client responded to the server
public class ClientInfo {
	
	private InetAddress address;
	private int port;
	private String name;
	private int id;
	
	// When server creates client info object and runs,this constructor/class that sets the private variable
	public ClientInfo(String name, int id, InetAddress address, int port) {
		this.name = name;
		this.id= id;
		this.address = address;
		this.port = port;	
	}
	
	// getters
	public String getName() {
		return name;
	}
	
	public int getID() {
		return id;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}

	
}
