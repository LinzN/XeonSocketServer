package de.nlinz.system.server.eva.server;

import java.io.ByteArrayOutputStream;

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