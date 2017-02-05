package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import model.ClientModel;
import view.ClientInput;
import view.ClientOutput;

public class ClientCore extends Thread {
	
	public static int port;
	public static String ip;
	byte[] buf = new byte[256];

	public static DatagramSocket clientSocket;
	public static Object serverLock = new Object();
	public static int secTillGameStart = 5;

	
	private boolean stop = false;
	private ClientModel clientModel;
	private ServerHandler serverHandler;
	private ClientInput ci;
	
	public ClientCore(String IPPort) {
		String[] split = IPPort.split(":");
		this.ip = split[0];
		this.port = Integer.parseInt(split[1]);
	}
	
	public void run() {
		try {
			this.clientSocket = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
			return;
		}
		clientModel = new ClientModel();
		ci = new ClientInput();
		try {
			serverHandler = new ServerHandler(ci, new ClientOutput(clientSocket, InetAddress.getByName(ip), port));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		System.out.println("Server is ready!");
		serverHandler.start();
		
		while(!stop) {
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			try {
				clientSocket.receive(p);
			} catch (IOException e) {
				System.err.println("Normal Exception :");
				e.printStackTrace();
				continue;
			}
			String message = new String(p.getData());
			message = message.substring(0, p.getLength());
			
			serverHandler.receiveMessage(message);
		
		}
	}

}
