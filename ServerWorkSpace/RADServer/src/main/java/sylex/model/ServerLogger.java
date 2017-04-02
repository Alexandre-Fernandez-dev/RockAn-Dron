package sylex.model;

import sylex.model.interfaces.IServerLogger;

public class ServerLogger implements IServerLogger {

	@Override
	public void systemMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void packetReceived(String ip, String message) {
		System.out.println(ip+": "+message);
	}


}
