package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import model.ServerCore;

public class PacketHandler extends Thread {
	public DatagramSocket s;
	private boolean stop = false;
	byte[] buf = new byte[256];
	
	@Override
	public void run() {
		while(!stop) {
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			try {
				s.receive(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(ServerCore.clientHandlers.containsKey(p.getAddress())) {
				HandleClient sender = ServerCore.clientHandlers.get(p.getAddress());
				sender.getSi()
			} else {
				//créer client
			}
		}
	}

	public void stop(boolean stop) {
		this.stop = stop;
	}

}
