package de.nlinz.javaSocket.server.interfaces;

import de.nlinz.javaSocket.server.run.ConnectedClient;
import de.nlinz.javaSocket.server.run.SocketServer;

/* Interface for the SocketServer*/
public interface ISocketServer {
	void onConnect(final ConnectedClient p0);

	void onDataRecieve(final ConnectedClient p0, String channel, final byte[] bytes);

	void onDisconnect(final ConnectedClient p0);

	void runTaskConnectedClient(final ConnectedClient p0);

	void runTaskSocketServer(final SocketServer p0);
}