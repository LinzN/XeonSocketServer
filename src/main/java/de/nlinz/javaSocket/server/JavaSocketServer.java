
package de.nlinz.javaSocket.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import de.nlinz.cookieSocketBungee.mask.CookieSocketBungeeMask;
import de.nlinz.javaSocket.server.events.SocketDataEvent;
import de.nlinz.javaSocket.server.events.SocketTypeEvent;
import de.nlinz.javaSocket.server.interfaces.IDataListener;
import de.nlinz.javaSocket.server.interfaces.ISocketServer;
import de.nlinz.javaSocket.server.interfaces.ITypeListener;
import de.nlinz.javaSocket.server.interfaces.SocketServerEventType;
import de.nlinz.javaSocket.server.run.ConnectedClient;
import de.nlinz.javaSocket.server.run.SocketServer;
import net.md_5.bungee.api.ProxyServer;

public class JavaSocketServer implements ISocketServer {

	/* Variables and instances */
	private SocketServer server;
	private String hostName;
	private int port;
	/* HashSets for storing the external eventlistener */
	private static HashSet<IDataListener> dataListeners = new HashSet<IDataListener>();
	private static HashSet<ITypeListener> typeListeners = new HashSet<ITypeListener>();

	/* Create this new Instance of EvaServer with the IEvaServer interface */
	public JavaSocketServer(String hostName, int port) {
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
	@Override
	public void runTaskSocketServer(final SocketServer server) {
		ProxyServer.getInstance().getScheduler().runAsync(CookieSocketBungeeMask.inst(), server);
	}

	/* Runnable to start a new ConnectedClient */
	@Override
	public void runTaskConnectedClient(final ConnectedClient mess) {
		ProxyServer.getInstance().getScheduler().runAsync(CookieSocketBungeeMask.inst(), mess);
	}

	/* Runnable for default type */
	public void runTask(final Runnable runnable) {
		ProxyServer.getInstance().getScheduler().runAsync(CookieSocketBungeeMask.inst(), runnable);
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
