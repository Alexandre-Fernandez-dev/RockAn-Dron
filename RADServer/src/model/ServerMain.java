package model;

public class ServerMain {

	public static void main(String[] args) {
		int port = args.length==1?Integer.parseInt(args[0]):1234;
		ServerCore core = new ServerCore(port);
		core.start();
	}

}
