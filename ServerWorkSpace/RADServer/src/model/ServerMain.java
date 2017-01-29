package model;

public class ServerMain {

	public static void main(String[] args) {
		int port = args.length==1?Integer.parseInt(args[0]):1236;
		System.out.println("Initialising ServerCore");
		ServerCore core = new ServerCore(port);
		core.start();
		System.out.println("Done");
	}

}
