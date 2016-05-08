
package de.nlinz.system.server.eva;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import de.nlinz.system.server.eva.interfaces.EvaEventType;
import de.nlinz.system.server.eva.interfaces.IDataListener;
import de.nlinz.system.server.eva.interfaces.IEvaServer;
import de.nlinz.system.server.eva.interfaces.ITypeListener;
import de.nlinz.system.server.eva.server.ConnectedClient;
import de.nlinz.system.server.eva.server.SocketDataEvent;
import de.nlinz.system.server.eva.server.SocketServer;
import de.nlinz.system.server.eva.server.SocketTypeEvent;

public class EvaServer implements IEvaServer {

	/* Variables and instances */
	private SocketServer server;
	private String hostName;
	private int port;
	private static HashSet<IDataListener> dataListeners = new HashSet<IDataListener>();
	private static HashSet<ITypeListener> typeListeners = new HashSet<ITypeListener>();

	/* Create this new Instance of EvaServer with the IEvaServer interface */
	public EvaServer(String hostName, int port) {
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

	/* Runnable to start the SocketServer */
	public void runTaskSocketServer(final SocketServer server) {
		Executors.newScheduledThreadPool(1).schedule(server, 0, TimeUnit.MILLISECONDS);
	}

	/* Runnable to start a new ConnectedClient */
	public void runTaskConnectedClient(final ConnectedClient mess) {
		Executors.newScheduledThreadPool(1).schedule(mess, 0, TimeUnit.MILLISECONDS);
	}

	/* Runnable for default type */
	public void runTask(final Runnable runnable) {
		Executors.newScheduledThreadPool(1).schedule(runnable, 0, TimeUnit.MILLISECONDS);
	}

	/* Call when a new client join the network */
	public void onConnect(final ConnectedClient mess) {
		final EvaEventType type = EvaEventType.CONNECT;
		final SocketTypeEvent event = new SocketTypeEvent(mess);
		for (ITypeListener listener : typeListeners) {
			if (listener.getType() == type) {
				listener.onTypeEvent(event);
			}
		}
		SocketDebug.sendNewClient(mess.getSocket().getRemoteSocketAddress().toString());

	}

	/* Call when a client leave the network */
	public void onDisconnect(final ConnectedClient mess) {
		final EvaEventType type = EvaEventType.DISCONNECT;
		final SocketTypeEvent event = new SocketTypeEvent(mess);
		for (ITypeListener listener : typeListeners) {
			if (listener.getType() == type) {
				listener.onTypeEvent(event);
			}
		}
		SocketDebug.sendDeleteClient(mess.getSocket().getRemoteSocketAddress().toString());

	}

	/* Call when a receiving data from a ConnectedClient */
	public void onDataRecieve(final ConnectedClient mess, final String channel, final byte[] bytes) {
		final SocketDataEvent event = new SocketDataEvent(mess, channel, bytes);
		for (IDataListener listener : dataListeners) {
			if (listener.getChannel().equalsIgnoreCase(channel)) {
				listener.onDataRecieve(event);
			}
		}

	}

	/* Send byte[] data to all connected clients */
	public void sendBytesOut(ByteArrayOutputStream bytes) {
		for (ConnectedClient sockMSG : this.getSocketServer().getConnectedClients()) {
			sockMSG.write(bytes);
		}
	}

	/* Register a new EVA DataListener */
	public static void registerDataListener(IDataListener listener) {
		dataListeners.add(listener);
	}

	/* Register a new EVA TypeListener */
	public static void registerTypeListener(ITypeListener listener) {
		typeListeners.add(listener);
	}

	/* Creating a new channel outputStream */
	public static DataOutputStream createChannel(ByteArrayOutputStream bytes, String channel) {
		DataOutputStream outStream = new DataOutputStream(bytes);
		try {
			outStream.writeUTF(channel);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return outStream;

	}

	/* Creating a new channel inputStream */
	public static DataInputStream createInputStream(byte[] bytes) {
		InputStream inputStream = new ByteArrayInputStream(bytes);
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		return dataInputStream;

	}

}
