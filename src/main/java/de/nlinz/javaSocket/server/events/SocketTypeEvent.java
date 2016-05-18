package de.nlinz.javaSocket.server.events;

import java.io.ByteArrayOutputStream;

import de.nlinz.javaSocket.server.run.ConnectedClient;

/* TypeEvent for available types like connect or disconnect*/
public class SocketTypeEvent {
	private ConnectedClient mess;

	public SocketTypeEvent(final ConnectedClient mess) {
		this.mess = mess;

	}

	/* Return the specific client which the event was fired */
	public ConnectedClient getMessenger() {
		return this.mess;
	}

	/* Send data to the specific client back */
	public void sendDataBack(final ByteArrayOutputStream bytes) {
		this.mess.write(bytes);
	}

}