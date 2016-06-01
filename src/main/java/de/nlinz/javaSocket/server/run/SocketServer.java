package de.nlinz.javaSocket.server.run;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import de.nlinz.javaSocket.server.interfaces.ISocketServer;

/* SocketServer. Check if a new client is connected*/
public class SocketServer implements Runnable {
	/* Variables */
	private ServerSocket server;
	private int port;
	private String host;
	private ISocketServer app;
	/* Stored all connected clients */
	ArrayList<ConnectedClient> connectedClients;

	public SocketServer(final ISocketServer app, final int port, final String host) {
		this.port = port;
		this.host = host;
		this.app = app;
		this.connectedClients = new ArrayList<ConnectedClient>();
		try {
			this.server = new ServerSocket();
		} catch (IOException ex) {
		}
	}

	public IOException start() {
		try {
			this.server = new ServerSocket();
			this.server.bind(new InetSocketAddress(this.host, this.port));
			this.app.runTask(this);
			return null;
		} catch (IOException e) {
			return e;
		}
	}

	/* Return the port of the socketConnection */
	public int getPort() {
		return this.port;
	}

	/* Return the Interface of the SocketServer */
	public ISocketServer getApp() {
		return this.app;
	}

	/* Return the SocketServer */
	public ServerSocket getServerSocket() {
		return this.server;
	}

	/* Get new clients if a client is trying to connect */
	@Override
	public void run() {
		while (!this.server.isClosed()) {
			try {
				final Socket socket = this.server.accept();
				socket.setTcpNoDelay(true);
				final ConnectedClient connectedClient = new ConnectedClient(this, socket);
				this.connectedClients.add(connectedClient);
				this.app.onConnect(connectedClient);
				this.app.runTask(connectedClient);
			} catch (IOException ex) {
			}
		}
	}

	/* Close this socketServer */
	public IOException close() {
		if (!this.server.isClosed()) {
			try {
				this.server.close();
				for (final ConnectedClient connectedClient : new ArrayList<ConnectedClient>(this.connectedClients)) {
					connectedClient.close();
				}
			} catch (IOException e) {
				return e;
			}
		}
		return null;
	}

	/* Return all connected clients */
	public ArrayList<ConnectedClient> getConnectedClients() {
		return new ArrayList<ConnectedClient>(this.connectedClients);
	}

	/* Check if the client is available */
	public boolean isEnabled() {
		return !this.server.isClosed();
	}
}