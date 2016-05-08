package de.nlinz.system.server.eva.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import de.nlinz.system.server.eva.interfaces.IEvaServer;

public class SocketServer implements Runnable {
	private ServerSocket server;
	private int port;
	private String host;
	private IEvaServer app;
	ArrayList<ConnectedClient> connectedClients;

	public SocketServer(final IEvaServer app, final int port, final String host) {
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
			this.app.runTaskSocketServer(this);
			return null;
		} catch (IOException e) {
			return e;
		}
	}

	public int getPort() {
		return this.port;
	}

	public IEvaServer getApp() {
		return this.app;
	}

	public ServerSocket getServerSocket() {
		return this.server;
	}

	public void run() {
		while (!this.server.isClosed()) {
			try {
				final Socket socket = this.server.accept();
				socket.setTcpNoDelay(true);
				final ConnectedClient connectedClient = new ConnectedClient(this, socket);
				this.connectedClients.add(connectedClient);
				this.app.onConnect(connectedClient);
				this.app.runTaskConnectedClient(connectedClient);
			} catch (IOException ex) {
			}
		}
	}

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

	public ArrayList<ConnectedClient> getConnectedClients() {
		return new ArrayList<ConnectedClient>(this.connectedClients);
	}

	public boolean isEnabled() {
		return !this.server.isClosed();
	}
}