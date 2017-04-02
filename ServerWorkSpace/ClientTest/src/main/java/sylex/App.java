package sylex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class App {

    public static void main(String[] args) throws IOException {
        boolean cont = true;
        InetAddress addr;

        addr = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);

        final DatagramSocket sock = new DatagramSocket();
        new Thread(new Runnable()
                {
                    @Override
                    public void run() {
                        while(!sock.isClosed()) {
                            byte[] buf = new byte[256];
                            DatagramPacket p = new DatagramPacket(buf, buf.length);
                            try {
                                sock.receive(p);
                                String message = new String(p.getData());
                                message = message.substring(0, p.getLength());
                                System.out.println(">> " + message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(cont) {
            String line = "";
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] data = line.getBytes();
            DatagramPacket p = new DatagramPacket(data, data.length, addr, port);
            try {
                sock.send(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        br.close();
        sock.close();

    }

}

