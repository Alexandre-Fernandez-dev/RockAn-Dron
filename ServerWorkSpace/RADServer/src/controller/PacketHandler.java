package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import model.ServerCore;
import model.ServerModel;
import view.ServerInput;
import view.ServerOutput;

public class PacketHandler extends Thread {
	private boolean stop = false;
	byte[] buf = new byte[256];
	
	@Override
	public void run() {
		while(!stop) {
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			try {
				ServerCore.serverSocket.receive(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
			HandleClient sender;
			if(ServerModel.clientHandlers.containsKey(p.getAddress())) {
				sender = ServerModel.clientHandlers.get(p.getAddress());
			} else {
				ServerOutput so = new ServerOutput(ServerCore.serverSocket, p.getAddress(), ServerCore.port);
				ServerInput si = new ServerInput();
				sender = new HandleClient(p.getAddress(), null, si, so);
				si.init(sender);
				ServerModel.clientHandlers.put(p.getAddress(), sender);
				Thread t = new Thread(sender);
				t.start();
			}
			String message = new String(p.getData());
			message.substring(0, p.getLength());
			sender.receiveMessage(message.substring(0, p.getLength()));
		}
	}

	public void stop(boolean stop) {
		this.stop = stop;
	}

}
