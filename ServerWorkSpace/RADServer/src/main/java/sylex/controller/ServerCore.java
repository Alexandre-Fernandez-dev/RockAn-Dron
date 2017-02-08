package sylex.controller;

import java.net.DatagramSocket;
import java.net.SocketException;

import sylex.model.ServerModel;

public class ServerCore extends Thread {
	public int port;
	public DatagramSocket serverSocket;
	public Object serverLock = new Object();
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	public static int secTillGameStart = 5;

	
	private boolean stop = false;
	//private ServerLogger logger = new ServerLogger();
	private ServerModel serverModel;
	private PacketHandler packetHandler;
	
	public ServerCore(int port) {
		this.port = port;
	}
	
	public void run() {
		try {
			serverSocket = new DatagramSocket(port);
		} catch (SocketException e1) {
			e1.printStackTrace();
			return;
		}
		serverModel = new ServerModel();
		packetHandler = new PacketHandler(serverSocket);
		System.out.println("Server is ready!");
		packetHandler.start();
		while(!stop) {
			//ServerModel.deleteGame(); EST CE QUE IL NE VAUDRAIT PAS MIEUX VIRER LE WHILE ??
			synchronized(serverLock) {
				try {
					//waiting for end of game.
					serverLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void stopServer() {
		this.stop = true;
		serverModel.stopServer();
		packetHandler.stopServer();
		try {
			packetHandler.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized(serverLock) {
			serverLock.notify();
		}
	}
}
