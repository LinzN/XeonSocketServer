package de.nlinz.javaSocket.server.events;

import java.io.ByteArrayOutputStream;

import de.nlinz.javaSocket.server.run.ConnectedClient;

public class SocketTypeEvent {
	private ConnectedClient mess;

	public SocketTypeEvent(final ConnectedClient mess) {
		this.mess = mess;

	}

	public ConnectedClient getMessenger() {
		return this.mess;
	}

	public void sendDataBack(final ByteArrayOutputStream bytes) {
		this.mess.write(bytes);
	}

}