package de.nlinz.javaSocket.server.interfaces;

import de.nlinz.javaSocket.server.run.ConnectedClient;

/* Interface for the SocketServer*/
public interface ISocketServer {
	void onConnect(final ConnectedClient p0);

	void onDataRecieve(final ConnectedClient p0, String channel, final byte[] bytes);

	void onDisconnect(final ConnectedClient p0);

	void runTask(final Runnable runnable);

}