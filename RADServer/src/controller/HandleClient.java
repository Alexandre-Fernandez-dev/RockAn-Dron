package controller;

import java.net.DatagramPacket;

import model.ServerLogger;
import model.interfaces.ServerModelEvents;
import view.ServerInput;
import view.ServerOutput;
import view.interfaces.ServerProtocol;

public class HandleClient implements Runnable, ServerProtocol, ServerModelEvents {
	
	private DatagramPacket packet;
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];
	
	private ServerInput si;
	private ServerOutput so;
	private ServerLogger logger;

	public HandleClient(DatagramPacket packet, ServerLogger logger) {
		super();
		this.packet = packet;
		this.logger = logger;
	}

	@Override
	public void run() {
		
	}

}
