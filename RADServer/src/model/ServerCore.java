package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.Client;
import controller.HandleClient;

public class ServerCore extends Thread {
	private int port;
	DatagramSocket serverSocket;
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	
	private boolean stop = false;
	private ServerLogger logger = new ServerLogger();
	public static HashMap<InetAddress, HandleClient> clientHandlers;
	
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
					
					String message = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
					logger.packetReceived(receivePacket.getAddress().toString(), message);
					new Thread(new HandleClient(receivePacket, logger)).start();
					receivePacket.setLength(receiveData.length);
				}
				catch (IOException e) {
					System.out.println("I/O error: " + e.toString());
					Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, e);
				}
				
			}
		}
		catch (SocketException e) {
			System.out.println("Could not bind port " + port);
			/* TODO: Logger */
		}
	}
}
