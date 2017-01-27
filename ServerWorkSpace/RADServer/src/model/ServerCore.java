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
import controller.PacketHandler;
import model.game.Game;
import view.ServerOutput;

public class ServerCore extends Thread {
	public static int port;
	public static DatagramSocket serverSocket;
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	
	private boolean stop = false;
	private ServerLogger logger = new ServerLogger();
	
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
		PacketHandler p = new PacketHandler();
		System.out.println("Server is ready!");
		p.start();
		while(!stop) {
			ServerModel.deleteGame();
			try {//peut être mettre un while avec une condition pour éviter les notify imprévus (peut arriver en JAVA)
				//waiting for user create game.
				serverSocket.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopServer() {
		this.stop = true;
	}
}
