package de.nlinz.system.server.eva.interfaces;

import de.nlinz.system.server.eva.server.ConnectedClient;
import de.nlinz.system.server.eva.server.SocketServer;

public interface IEvaServer {
	void onConnect(final ConnectedClient p0);

	void onDataRecieve(final ConnectedClient p0, String channel, final byte[] bytes);

	void onDisconnect(final ConnectedClient p0);

	void runTaskConnectedClient(final ConnectedClient p0);

	void runTaskSocketServer(final SocketServer p0);
}