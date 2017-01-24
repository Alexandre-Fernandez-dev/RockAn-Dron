package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import model.interfaces.IServerLogger;

public class ServerCore extends Thread {
	private int port;
	DatagramSocket serverSocket;
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	
	private boolean stop = false;
	private IServerLogger logger = null;
	
	public ServerCore(int port) {
		this.port = port;
	}
	
	public void run() {
		try {
			serverSocket = new DatagramSocket(port);
			System.out.println("Server is ready!");
			while(!stop) {
				try {
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					/* Waiting for 2 clients */
					serverSocket.receive(receivePacket);
					/* TODO: Logger - received data */
					String message = new String(receivePacket.getData());
					/* TODO: HandleClient */
					receivePacket.setLength(receiveData.length);
				}
				catch (IOException e) {
					System.out.println("I/O error: " + e.toString());
					/* TODO: Logger */
				}
				
			}
		}
		catch (SocketException e) {
			System.out.println("Could not bind port " + port);
			/* TODO: Logger */
		}
	}
}
