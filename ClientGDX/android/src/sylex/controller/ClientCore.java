package sylex.controller;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import sylex.model.ClientModel;
import sylex.view.ClientInput;
import sylex.view.ClientOutput;

public class ClientCore extends Thread {
	
	public static int port;
	public static InetAddress ip;
	byte[] buf = new byte[256];

	public static DatagramSocket clientSocket;
	public static Object serverLock = new Object();
	public static int secTillGameStart = 5;

	
	private boolean stop = false;
	private ClientModel clientModel;
	private ServerHandler serverHandler;
	private ClientInput ci;
	
	public ClientCore(String IPPort) throws SocketException, UnknownHostException {
		String[] split = IPPort.split(":");
		this.ip = InetAddress.getByName(split[0]);
		this.port = Integer.parseInt(split[1]);
		Log.d("D", "IP : \"" + split[0] +  "\" Port : " + port);
		this.clientSocket = new DatagramSocket();
		ci = new ClientInput();
		serverHandler = new ServerHandler(ci, new ClientOutput(clientSocket, ip, port));
		ci.init(serverHandler);
		ClientModel.setHandler(serverHandler);
	}
	
	public void run() {
		System.out.println("Server is ready!");
		serverHandler.start();
		
		while(!stop) {
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			try {
				synchronized (serverLock) {
					serverLock.notify();
				}
				clientSocket.receive(p);
			} catch (IOException e) {
				System.err.println("Normal Exception :");
				e.printStackTrace();
				continue;
			}
			String message = new String(p.getData());
			message = message.substring(0, p.getLength());
			Log.d("Handler message",message);
			
			serverHandler.receiveMessage(message);
		
		}
	}

	public void stopCore() {
		this.stop = true;
		this.interrupt();
	}

}
