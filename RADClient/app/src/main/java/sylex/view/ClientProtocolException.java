package sylex.view;

import java.io.IOException;

@SuppressWarnings("serial")
public class ClientProtocolException extends IOException {

	public ClientProtocolException(String message) {
		super(message);
	}

}
