
package de.nlinz.javaSocket.server;

import java.io.IOException;
import java.util.HashSet;

import de.nlinz.javaSocket.server.events.SocketDataEvent;
import de.nlinz.javaSocket.server.events.SocketTypeEvent;
import de.nlinz.javaSocket.server.interfaces.IDataListener;
import de.nlinz.javaSocket.server.interfaces.IServerMask;
import de.nlinz.javaSocket.server.interfaces.ISocketServer;
import de.nlinz.javaSocket.server.interfaces.ITypeListener;
import de.nlinz.javaSocket.server.interfaces.SocketServerEventType;
import de.nlinz.javaSocket.server.run.ConnectedClient;
import de.nlinz.javaSocket.server.run.SocketServer;

public class SocketServerInitialisator implements ISocketServer {

	/* Variables and instances */
	private SocketServer server;
	private String hostName;
	private int port;
	private IServerMask mask;
	public static SocketServerInitialisator inst;
	/* HashSets for storing the external eventlistener */
	public HashSet<IDataListener> dataListeners = new HashSet<IDataListener>();
	public HashSet<ITypeListener> typeListeners = new HashSet<ITypeListener>();

	/* Create this new Instance of EvaServer with the IEvaServer interface */
	public SocketServerInitialisator(IServerMask mask, String hostName, int port) {
		this.mask = mask;
		this.hostName = hostName;
		this.port = port;
	}

	/* Get this running SocketServer instance */
	public SocketServer getSocketServer() {
		return this.server;
	}

	/* Starting socketServer and prepare for incoming clients */
	public boolean start() {
		this.server = new SocketServer(this, this.port, this.hostName);
		final IOException err = this.server.start();
		if (err != null) {
			SocketDebug.sendStartError(this.server.getPort());
			err.printStackTrace();
			return false;
		}
		SocketDebug.sendStartSuccess(this.server.getServerSocket().getLocalSocketAddress().toString());

		return true;
	}

	/* Stopping the socketServer and closing all connected clients */
	public boolean stop() {
		final IOException err = this.server.close();
		if (err != null) {
			SocketDebug.sendShutdownError(this.server.getPort());
			err.printStackTrace();
			return false;
		}

		SocketDebug.sendShutdownSuccess();
		return true;
	}

	/* Runnable for default type */
	@Override
	public void runTask(final Runnable runnable) {
		this.mask.serverSchedulerAsync(runnable);
	}

	/* Call when a new client join the network */
	@Override
	public void onConnect(final ConnectedClient mess) {
		final SocketServerEventType type = SocketServerEventType.CONNECT;
		final SocketTypeEvent event = new SocketTypeEvent(mess);
		for (ITypeListener listener : typeListeners) {
			if (listener.getType() == type) {
				listener.onTypeEvent(event);
			}
		}
		SocketDebug.sendNewClient(mess.getSocket().getRemoteSocketAddress().toString());

	}

	/* Call when a client leave the network */
	@Override
	public void onDisconnect(final ConnectedClient mess) {
		final SocketServerEventType type = SocketServerEventType.DISCONNECT;
		final SocketTypeEvent event = new SocketTypeEvent(mess);
		for (ITypeListener listener : typeListeners) {
			if (listener.getType() == type) {
				listener.onTypeEvent(event);
			}
		}
		SocketDebug.sendDeleteClient(mess.getSocket().getRemoteSocketAddress().toString());

	}

	/* Call when a receiving data from a ConnectedClient */
	@Override
	public void onDataRecieve(final ConnectedClient mess, final String channel, final byte[] bytes) {
		final SocketDataEvent event = new SocketDataEvent(mess, channel, bytes);
		for (final IDataListener listener : dataListeners) {
			if (listener.getChannel().equalsIgnoreCase(channel)) {
				this.mask.serverSchedulerSync(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						listener.onDataRecieve(event);
					}

				});

			}
		}

	}

}
