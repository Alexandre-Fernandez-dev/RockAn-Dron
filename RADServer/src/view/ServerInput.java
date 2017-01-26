package view;

import java.io.IOException;
import java.net.DatagramPacket;

import view.interfaces.ServerProtocol;

public class ServerInput {
	ServerProtocol handler;
	DatagramPacket in;
	boolean stop = false;
	
	public ServerInput(ServerProtocol handler, DatagramPacket in) throws IOException {
		super();
		this.handler = handler;
		this.in = in;
	}
	
	public boolean parseMessage(String message) {
		String words[] = message.split(" ");
		switch(message) {
		case "" :
		break;
		}
		return false;
	}
	
}
