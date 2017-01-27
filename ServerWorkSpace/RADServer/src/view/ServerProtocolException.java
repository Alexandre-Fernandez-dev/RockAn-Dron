package view;

import java.io.IOException;

@SuppressWarnings("serial")
public class ServerProtocolException extends IOException {

	public ServerProtocolException(String message) {
		super(message);
	}

}
