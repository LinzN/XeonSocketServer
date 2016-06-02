package de.nlinz.javaSocket.server.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.nlinz.javaSocket.server.SocketServerInitialisator;
import de.nlinz.javaSocket.server.interfaces.IDataListener;
import de.nlinz.javaSocket.server.interfaces.ITypeListener;
import de.nlinz.javaSocket.server.run.ConnectedClient;

public class XeonSocketServerManager {
	/* Send byte[] data to all connected clients */
	public void sendData(ByteArrayOutputStream bytes) {
		for (ConnectedClient sockMSG : SocketServerInitialisator.inst.getSocketServer().getConnectedClients()) {
			sockMSG.write(bytes);
		}
	}

	public void sendData(ConnectedClient client, ByteArrayOutputStream bytes) {
		client.write(bytes);
	}

	/* Register a new EVA DataListener */
	public static void registerDataListener(IDataListener listener) {
		SocketServerInitialisator.inst.dataListeners.add(listener);
	}

	/* Register a new EVA TypeListener */
	public static void registerTypeListener(ITypeListener listener) {
		SocketServerInitialisator.inst.typeListeners.add(listener);
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
	public static DataInputStream readDataInput(byte[] bytes) {
		InputStream inputStream = new ByteArrayInputStream(bytes);
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		return dataInputStream;

	}
}
