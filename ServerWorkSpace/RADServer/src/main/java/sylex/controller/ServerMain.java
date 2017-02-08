package sylex.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerMain {

	public static void main(String[] args) throws IOException {
		int port = args.length==1?Integer.parseInt(args[0]):1235;
		System.out.println("Initialising ServerCore");
		ServerCore core = new ServerCore(port);
		core.start();
		System.out.println("Done");
		
		boolean stop = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(!stop) {
			if(br.readLine().equals("QUIT")) {
				stop = true;
			} else {
				System.out.println("Error : Unknown command. Type \"QUIT\" to quit...");
			}
		}
		core.stopServer();
		br.close();
	}

}
