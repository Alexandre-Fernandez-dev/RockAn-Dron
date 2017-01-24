package controller;

import java.net.DatagramSocket;

import model.interfaces.ServerModelEvents;
import view.interfaces.ServerProtocol;

public class HandleClient implements Runnable, ServerProtocol, ServerModelEvents {
	
	private DatagramSocket serverSocket;
	byte[] receiveData = new byte[1024];
	byte[] sendData = new byte[1024];

	@Override
	public void run() {
		
	}

}
