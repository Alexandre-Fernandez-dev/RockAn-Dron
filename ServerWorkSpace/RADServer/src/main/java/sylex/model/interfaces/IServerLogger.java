package sylex.model.interfaces;

public interface IServerLogger {
	public void systemMessage(String message);
	public void packetReceived(String ip, String message);
}
