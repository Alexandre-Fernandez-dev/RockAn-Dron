package sylex.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import sylex.model.ServerModel;
import sylex.view.ServerInput;
import sylex.view.ServerOutput;

public class PacketHandler extends Thread {
    private boolean stop = false;
    byte[] buf = new byte[256];
    private DatagramSocket socket;

    public PacketHandler(DatagramSocket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        while(!stop) {
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(p);
            } catch (IOException e) {
                System.err.println("Normal Exception :");
                e.printStackTrace();
                continue;
            }
            ClientHandler sender;
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
                ServerOutput so = new ServerOutput(socket, p.getAddress(), p.getPort());
                ServerInput si = new ServerInput();
                sender = new ClientHandler(p.getAddress(), null, si, so);//sdfokfgrjnf
                si.init(sender);
                //ServerModel.clientHandlers.put(p.getAddress(), sender);
                sender.start();
            }
            try {
            sender.receiveMessage(message);
            }catch(Exception e) {
            	System.out.println(message);
            	e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        this.stop = true;
        socket.close();
    }

}
