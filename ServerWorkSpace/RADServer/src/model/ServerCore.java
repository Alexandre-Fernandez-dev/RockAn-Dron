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
	public static Object serverLock = new Object();
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	public static int secTillGameStart = 5;

	
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
		ServerModel sm = new ServerModel();
		PacketHandler p = new PacketHandler();
		System.out.println("Server is ready!");
		p.start();
		while(!stop) {
			ServerModel.deleteGame();
			synchronized(serverLock) {
				try {//peut �tre mettre un while avec une condition pour �viter les notify impr�vus (peut arriver en JAVA)
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
	}
}
