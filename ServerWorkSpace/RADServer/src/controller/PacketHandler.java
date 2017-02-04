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
				System.err.println("Normal Exception :");
				e.printStackTrace();
				continue;
			}
			HandleClient sender;
			String message = new String(p.getData());
			message = message.substring(0, p.getLength());

			if(message.charAt(0) != 'C') {
				int fspace = message.indexOf(" ");
				String idCli = message.substring(0, fspace);
				System.out.println(idCli);
				int idClient = Integer.parseInt(idCli);
				System.out.println(idClient);
				message = message.substring(fspace+1);
				sender = ServerModel.clientHandlers.get(idClient);
			} else {
				ServerOutput so = new ServerOutput(ServerCore.serverSocket, p.getAddress(), p.getPort());
				ServerInput si = new ServerInput();
				sender = new HandleClient(p.getAddress(), null, si, so);
				si.init(sender);
				//ServerModel.clientHandlers.put(p.getAddress(), sender);
				sender.start();
			}
			sender.receiveMessage(message);
		}
	}
	
	public void stopServer() {
		this.stop = true;
		ServerCore.serverSocket.close();
	}

}
