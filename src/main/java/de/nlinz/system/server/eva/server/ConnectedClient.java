package de.nlinz.system.server.eva.server;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

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

	public void run() {
		try {
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			while (this.server.isEnabled() && this.socket.isConnected() && !this.socket.isClosed()) {

				String channel = inputStream.readUTF();

				if (channel == null || channel.isEmpty()) {
					continue;
				}

				byte[] bytes = new byte[(int) inputStream.available()];
				this.inputStream.readFully(bytes);
				this.server.getApp().onDataRecieve(this, channel, bytes);

			}
		} catch (IOException e2) {
			this.close();
		}
	}

	public SocketServer getServer() {
		return this.server;
	}

	public boolean isConnectedAndOpened() {
		return this.getSocket().isConnected() && !this.getSocket().isClosed();
	}

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

	public Socket getSocket() {
		return this.socket;
	}

	public UUID getClientUUID() {
		return this.clientUUID;
	}
}