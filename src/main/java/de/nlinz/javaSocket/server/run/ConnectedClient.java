package de.nlinz.javaSocket.server.run;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

/* Create this for every new client.*/
public class ConnectedClient implements Runnable {
	private Socket socket;
	private SocketServer server;
	private DataInputStream inputStream;
	private UUID clientUUID;

	public ConnectedClient(final SocketServer socketServer, final Socket socket) {
		this.socket = socket;
		this.server = socketServer;
		this.clientUUID = UUID.randomUUID();
	}

	/* Runnable for check if the client send bytes */
	@Override
	public void run() {
		try {
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			while (this.server.isEnabled() && this.socket.isConnected() && !this.socket.isClosed()) {

				String channel = inputStream.readUTF();

				if (channel == null || channel.isEmpty()) {
					continue;
				}

				byte[] bytes = new byte[inputStream.available()];
				this.inputStream.readFully(bytes);
				this.server.getApp().onDataRecieve(this, channel, bytes);

			}
		} catch (IOException e2) {
			this.close();
		}
	}

	/* Return the SocketServer */
	public SocketServer getServer() {
		return this.server;
	}

	/* Check if the client is connected and available */
	public boolean isConnectedAndOpened() {
		return this.getSocket().isConnected() && !this.getSocket().isClosed();
	}

	/* Send data to this client */
	public void write(ByteArrayOutputStream bytes) {
		try {
			OutputStream out = this.socket.getOutputStream();
			out.write(bytes.toByteArray());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* Close this client */
	public void close() {
		if (!this.socket.isClosed()) {
			try {
				this.socket.close();
			} catch (IOException e) {

			}
			this.server.connectedClients.remove(this);
			this.server.getApp().onDisconnect(this);
		}
	}

	/* Return the socket */
	public Socket getSocket() {
		return this.socket;
	}

	/* Return the UUID of this client */
	public UUID getClientUUID() {
		return this.clientUUID;
	}
}