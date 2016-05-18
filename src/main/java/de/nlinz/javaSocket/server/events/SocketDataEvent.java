package de.nlinz.javaSocket.server.events;

import java.io.ByteArrayOutputStream;

import de.nlinz.javaSocket.server.run.ConnectedClient;

/* Event for incoming valid data*/
public class SocketDataEvent {
	private ConnectedClient mess;
	private String channel;
	private byte[] bytes;

	public SocketDataEvent(final ConnectedClient mess, final String channel, final byte[] bytes) {
		this.mess = mess;
		this.channel = channel;
		this.bytes = bytes;
	}

	/* Return the channel which the data was send */
	public String getChannel() {
		return this.channel;
	}

	/* Return the data in bytes from the event */
	public byte[] getStreamBytes() {
		return this.bytes;
	}

	/* Return the specific client where the data send from */
	public ConnectedClient getMessenger() {
		return this.mess;
	}

	/* Send bytes back to the specific client */
	public void sendDataBack(final ByteArrayOutputStream bytes) {
		this.mess.write(bytes);
	}

}